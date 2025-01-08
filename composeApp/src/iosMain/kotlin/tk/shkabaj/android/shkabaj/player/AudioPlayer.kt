package tk.shkabaj.android.shkabaj.player

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.AVPlayerItemStatusFailed
import platform.AVFoundation.AVPlayerTimeControlStatusPaused
import platform.AVFoundation.AVPlayerTimeControlStatusPlaying
import platform.AVFoundation.addPeriodicTimeObserverForInterval
import platform.AVFoundation.asset
import platform.AVFoundation.currentItem
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.rate
import platform.AVFoundation.removeTimeObserver
import platform.AVFoundation.replaceCurrentItemWithPlayerItem
import platform.AVFoundation.seekToTime
import platform.AVFoundation.timeControlStatus
import platform.CoreMedia.CMTime
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.NSKeyValueChangeNewKey
import platform.Foundation.NSKeyValueChangeOldKey
import platform.Foundation.NSKeyValueObservingOptionNew
import platform.Foundation.NSKeyValueObservingOptionOld
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSURL
import platform.Foundation.addObserver
import platform.darwin.Float64
import platform.darwin.NSEC_PER_SEC
import platform.darwin.NSObject
import platform.foundation.NSKeyValueObservingProtocol
import kotlin.time.DurationUnit
import kotlin.time.toDuration

actual class AudioPlayer {

    enum class KVOKey(val key: String) {
        TIME_CONTROL_STATUS("timeControlStatus"), STATUS("status")
    }

    actual var eventsHandler: ((AudioPlayerEvent) -> Unit)? = null
    actual var onError: ((String?) -> Unit)? = null

    private var playerItems = mutableListOf<PlayerItem>()
    private val avAudioPlayer = AVPlayer()
    private val isPlaying: Boolean
        get() = avAudioPlayer.timeControlStatus != AVPlayerTimeControlStatusPaused

    @OptIn(ExperimentalForeignApi::class)
    private val currentItemDuration: Float64?
        get() = avAudioPlayer.currentItem?.asset?.let { CMTimeGetSeconds(it.duration) }

    private var currentItemIndex = -1
    private var timeObserver: Any? = null
    private var remoteControlsTargets = listOf<Any>()

    @OptIn(ExperimentalForeignApi::class)
    private val observer: (CValue<CMTime>) -> Unit =  { time: CValue<CMTime> ->
        val rawTime: Float64 = CMTimeGetSeconds(time)
        val currentTime = rawTime.toDuration(DurationUnit.SECONDS).inWholeSeconds

        val durationCMTime = currentItemDuration
        durationCMTime?.let {
            val duration = if (it.isNaN()) 0 else it.toDuration(DurationUnit.SECONDS).inWholeSeconds

            val progress = if (duration > 0) currentTime.toFloat() / duration.toFloat() else 0F
            val upd = AudioPlayerEvent.UpdateTime(
                current = currentTime * 1000, // to ms
                duration = duration * 1000, // to ms
                progress = progress
            )
            eventsHandler?.invoke(upd)

            MPNowPlayingInfoManager.updateElapsedPlaybackTime(currentTime.toFloat())
            MPNowPlayingInfoManager.updatePlaybackDuration(duration.toFloat())
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private val kvoObserver: NSObject = object : NSObject(), NSKeyValueObservingProtocol {
        override fun observeValueForKeyPath(
            keyPath: String?,
            ofObject: Any?,
            change: Map<Any?, *>?,
            context: COpaquePointer?
        ) {
            when(keyPath) {
                KVOKey.TIME_CONTROL_STATUS.key -> {
                    val newValue = change?.get(NSKeyValueChangeNewKey)
                    val newState = when (newValue) {
                        AVPlayerTimeControlStatusPlaying -> AudioPlayerState.PLAY
                        AVPlayerTimeControlStatusPaused -> AudioPlayerState.STOP
                        else -> AudioPlayerState.BUFFERING
                    }
                    MPNowPlayingInfoManager.updatePlaybackRate(avAudioPlayer.rate)
                    eventsHandler?.invoke(AudioPlayerEvent.StateUpdated(newState))
                }
                KVOKey.STATUS.key -> {
                    val item = avAudioPlayer.currentItem ?: return
                    if (item.status == AVPlayerItemStatusFailed) {
                        item.error?.let { onError?.invoke(it.description) }
                    }
                }
            }
        }
    }

    init {
        initObservers()
        //INFO: Enable MPRemoteControls on app start for CarPlay
        // If remote controls are disabled, carplay don't work
        setupRemoteControls()
    }

    @OptIn(ExperimentalForeignApi::class)
    fun initObservers() {
        avAudioPlayer.addObserver(
            observer = kvoObserver,
            forKeyPath = KVOKey.TIME_CONTROL_STATUS.key,
            options = NSKeyValueObservingOptionNew or NSKeyValueObservingOptionOld,
            context = null
        )
    }

    actual fun play() {
        avAudioPlayer.play()
    }

    actual fun pause() {
        avAudioPlayer.pause()
    }

    actual fun next() {
        val canNext = (currentItemIndex + 1) < playerItems.size
        if (canNext) {
            playWithIndex(currentItemIndex + 1)
        }
    }

    actual fun prev() {
        val canPrev = (currentItemIndex - 1) >= 0
        if (canPrev) {
            playWithIndex(currentItemIndex - 1)
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun seekToPercent(percent: Float) {
        currentItemDuration?.let {
            seekToTime(CMTimeMakeWithSeconds(it * percent, NSEC_PER_SEC.toInt()))
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun seekTo(pos: Long) {
        val sec = pos.toDouble() / 1000
        seekToTime(CMTimeMakeWithSeconds(sec, NSEC_PER_SEC.toInt()))
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun seekToTime(cmTime: CValue<CMTime>) {
        MPNowPlayingInfoManager.updatePlaybackRate(0F) // stop track in NowPlayingInfo
        avAudioPlayer.currentItem?.seekToTime(time = cmTime, completionHandler = {})
        MPNowPlayingInfoManager.updateElapsedPlaybackTime(CMTimeGetSeconds(cmTime).toFloat())
        MPNowPlayingInfoManager.updatePlaybackRate(avAudioPlayer.rate) // continue play with current rate
    }

    actual fun play(items: List<PlayerItem>, startIndex: Int) {
        playerItems = items.toMutableList()
        playWithIndex(startIndex)
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun addTimeObserver() {
        if (timeObserver != null) return
        val interval = CMTimeMakeWithSeconds(1.0, NSEC_PER_SEC.toInt())
        timeObserver = avAudioPlayer.addPeriodicTimeObserverForInterval(interval, null, observer)
        NSNotificationCenter.defaultCenter.addObserverForName(
            name = AVPlayerItemDidPlayToEndTimeNotification,
            `object` = avAudioPlayer.currentItem,
            queue = NSOperationQueue.mainQueue,
            usingBlock = {
                next()
            }
        )
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun playWithIndex(index: Int) {
        eventsHandler?.invoke(AudioPlayerEvent.StateUpdated(AudioPlayerState.BUFFERING))

        val playItem = playerItems.getOrNull(index) ?: return
        currentItemIndex = index

        val isPodcast = playItem is PlayerItem.Podcast
        if (isPodcast) {
            addTimeObserver()
        } else {
            removeTimeObserver()
        }

        val url = NSURL(string = playItem.mediaUrl)
        val avPlayerItem = AVPlayerItem(url)
        avPlayerItem.addObserver(
            observer = kvoObserver,
            forKeyPath = KVOKey.STATUS.key,
            options = NSKeyValueObservingOptionNew,
            context = null
        )

        eventsHandler?.invoke(AudioPlayerEvent.StartPlaying(playItem))
        avAudioPlayer.replaceCurrentItemWithPlayerItem(avPlayerItem)
        avAudioPlayer.play()

        MPNowPlayingInfoManager.setItemInfo(
            itemName = playItem.title ?: "",
            artistName = (if (isPodcast) playItem.subtitle2 else playItem.subtitle) ?: "",
            imageUrl = playItem.imageUrl,
            isLiveStream = playItem is PlayerItem.Radio
        )
        MPNowPlayingInfoManager.setItemsCountInfo(index, playerItems.count())
        setupRemoteControls()
        MPRemoteControlsManager.setEnableStateForRemotePlayerButtons(true)
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun stop() {
        removeTimeObserver()
        avAudioPlayer.pause()
        avAudioPlayer.currentItem?.seekToTime(CMTimeMakeWithSeconds(0.0, NSEC_PER_SEC.toInt()))

        removeRemoteControls()
        MPNowPlayingInfoManager.removeNowPlayingInfo()
        eventsHandler?.invoke(AudioPlayerEvent.StateUpdated(AudioPlayerState.STOP))
    }

    private fun removeTimeObserver() {
        timeObserver?.let {
            avAudioPlayer.removeTimeObserver(it)
            timeObserver = null
        }
    }

    actual fun cleanUp() {
        stop()
        currentItemIndex = -1
        playerItems.clear()
    }


    private fun setupRemoteControls() {
        removeRemoteControls()
        remoteControlsTargets = MPRemoteControlsManager.addRemoteControlsHandlers { action->
            when(action) {
                MPRemoteControlsManager.Action.PLAY -> play()
                MPRemoteControlsManager.Action.PAUSE -> pause()
                MPRemoteControlsManager.Action.NEXT -> next()
                MPRemoteControlsManager.Action.PREV -> prev()
            }
        }
    }

    private fun removeRemoteControls() {
        // clear remote controls actions, but don't disable they
        MPRemoteControlsManager.removeRemoteControlsHandlers(remoteControlsTargets)
        remoteControlsTargets = emptyList()
    }

}