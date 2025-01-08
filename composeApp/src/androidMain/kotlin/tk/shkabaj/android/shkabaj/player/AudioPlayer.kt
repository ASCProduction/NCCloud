package tk.shkabaj.android.shkabaj.player

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tk.shkabaj.android.shkabaj.extensions.launchPeriodAsync

actual class AudioPlayer: KoinComponent {

    companion object {
        private const val KEY_SITE = "key_site"
        private const val KEY_SUBTITLE_2 = "key_subtitle_2"
        private const val KEY_SUBTITLE_3 = "key_subtitle_3"
        private const val KEY_LISTENING_INFO = "key_listening_info"
    }

    private var playerItems = mutableListOf<PlayerItem>()
    private var lastPlayingItem: PlayerItem? = null
    private var playbackJob: Job? = null

    private val context by inject<Context>()
    private var mediaControllerFuture: ListenableFuture<MediaController>

    private val mediaController: MediaController?
        get() = if (mediaControllerFuture.isDone) mediaControllerFuture.get() else null

    actual var eventsHandler: ((AudioPlayerEvent) -> Unit)? = null
    actual var onError: ((String?) -> Unit)? = null

    init {
        val serviceComponent = ComponentName(context, PlaybackService::class.java)
        val sessionToken = SessionToken(context, serviceComponent)
        mediaControllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        mediaControllerFuture.addListener({ controllerListener() }, MoreExecutors.directExecutor())
    }

    private fun controllerListener() {
        mediaController?.addListener(object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)
                Log.d("AppMediaPlayerImpl", "Player event: ${events}")

                val item = playerItems.getOrNull(player.currentMediaItemIndex)
                if (item != lastPlayingItem) {
                    lastPlayingItem = item
                    item?.let { eventsHandler?.invoke(AudioPlayerEvent.StartPlaying(it)) }

                    stopPlaybackListener()
                    if (item is PlayerItem.Podcast) {
                        startPlaybackListener()
                    }
                }

                val state = playbackStateToAudioPlayerState(player.playbackState)
                eventsHandler?.invoke(AudioPlayerEvent.StateUpdated(state))
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                val state = playbackStateToAudioPlayerState(playbackState)
                eventsHandler?.invoke(AudioPlayerEvent.StateUpdated(state))

                if (playbackState == Player.STATE_ENDED) {
                    if (mediaController?.isPlaying == true) {
                        next()
                    }
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                val playbackState = mediaController?.playbackState ?: return
                val state = playbackStateToAudioPlayerState(playbackState)
                eventsHandler?.invoke(AudioPlayerEvent.StateUpdated(state))
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                onError?.invoke(error.message)

                val idx = mediaController?.currentMediaItemIndex ?: return
                //INFO: reset items after error, prepare for playing next/prev item
                setMediaItems(playerItems, idx, false)
            }
        })
    }

    actual fun play(items: List<PlayerItem>, startIndex: Int) {
        playerItems = items.toMutableList()
        setMediaItems(items, startIndex, true)
    }

    actual fun play() {
        mediaController?.play()
    }

    actual fun pause() {
        mediaController?.pause()
    }

    actual fun next() {
        mediaController?.seekToNextMediaItem()
    }

    actual fun prev() {
        mediaController?.seekToPreviousMediaItem()
    }

    actual fun seekToPercent(percent: Float) {
        val duration = mediaController?.duration ?: return
        val time = duration * percent
        mediaController?.seekTo(time.toLong())
    }

    actual fun seekTo(pos: Long) {
        mediaController?.seekTo(pos)
    }

    actual fun cleanUp() {
        stopPlaybackListener()
        mediaController?.stop()
        MediaController.releaseFuture(mediaControllerFuture)
    }

    private fun playbackStateToAudioPlayerState(value: Int): AudioPlayerState {
        return when(value) {
            Player.STATE_ENDED -> AudioPlayerState.STOP
            Player.STATE_READY -> {
                if (mediaController?.isPlaying == true) {
                    AudioPlayerState.PLAY
                } else {
                    AudioPlayerState.STOP
                }
            }
            else -> AudioPlayerState.BUFFERING
        }
    }

    private fun setMediaItems(items: List<PlayerItem>, startIndex: Int, play: Boolean) {
        val mediaItems = items.map { item ->
            MediaItem.Builder()
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(item.title ?: "")
                        .setSubtitle(item.subtitle ?: "")
                        .setArtist(item.subtitle)
                        .setArtworkUri(Uri.parse(item.imageUrl ?: ""))
                        .setExtras(
                            bundleOf(
                                KEY_SITE to item.site,
                                KEY_SUBTITLE_3 to item.subtitle3,
                                KEY_LISTENING_INFO to item.listeningInfo,
                            )
                        )
                        .build()
                )
                .build()
        }
        mediaController?.apply {
            setMediaItems(mediaItems, startIndex, 0)
            prepare()
            if (play) {
                play()
            }
        }
    }

    private fun startPlaybackListener() {
        if (playbackJob?.isActive == true) return
        playbackJob = CoroutineScope(Dispatchers.Main).launchPeriodAsync(1000, true) {
            notifyPlayback()
        }
    }

    private fun stopPlaybackListener() {
        playbackJob?.cancel()
    }

    private fun notifyPlayback() {
        val controller = mediaController ?: return
        eventsHandler?.invoke(AudioPlayerEvent.UpdateTime(
            current = controller.currentPosition.coerceAtLeast(0),
            duration = controller.duration.coerceAtLeast(0),
            progress = (controller.currentPosition.toDouble() / controller.duration.toDouble()).toFloat()
        ))
    }

}