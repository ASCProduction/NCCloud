package tk.shkabaj.android.shkabaj.player

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import platform.MediaPlayer.MPMediaItemArtwork
import platform.MediaPlayer.MPMediaItemPropertyArtist
import platform.MediaPlayer.MPMediaItemPropertyArtwork
import platform.MediaPlayer.MPMediaItemPropertyPlaybackDuration
import platform.MediaPlayer.MPMediaItemPropertyTitle
import platform.MediaPlayer.MPNowPlayingInfoCenter
import platform.MediaPlayer.MPNowPlayingInfoPropertyElapsedPlaybackTime
import platform.MediaPlayer.MPNowPlayingInfoPropertyIsLiveStream
import platform.MediaPlayer.MPNowPlayingInfoPropertyPlaybackQueueCount
import platform.MediaPlayer.MPNowPlayingInfoPropertyPlaybackQueueIndex
import platform.MediaPlayer.MPNowPlayingInfoPropertyPlaybackRate
import platform.MediaPlayer.MPNowPlayingPlaybackStatePaused
import platform.MediaPlayer.MPNowPlayingPlaybackStatePlaying
import platform.UIKit.UIImage

interface MPNowPlayingInfoArtworkLoader {
    suspend fun loadArtwork(imageUrl: String): UIImage?
}

object MPNowPlayingInfoManager: KoinComponent {

    private val artworkLoader by inject<MPNowPlayingInfoArtworkLoader>()
    private val nowPlayingInfoCenter = MPNowPlayingInfoCenter.defaultCenter()
    private val nowPlayingInfo: MutableMap<Any?, Any?> = mutableMapOf()

    private var artworkLoadJob: Job? = null

    fun setItemInfo(itemName: String, artistName: String,
                    imageUrl: String?,
                    isLiveStream: Boolean) {
        artworkLoadJob?.cancel()
        val artworkItem = UIImage.imageNamed("artworkImage")?.let {
            MPMediaItemArtwork(image = it)
        }
        nowPlayingInfo[MPMediaItemPropertyArtwork] = artworkItem
        nowPlayingInfo[MPMediaItemPropertyTitle] = itemName
        nowPlayingInfo[MPMediaItemPropertyArtist] = artistName
        nowPlayingInfo[MPNowPlayingInfoPropertyPlaybackRate] = 0.0
        nowPlayingInfo[MPNowPlayingInfoPropertyIsLiveStream] = isLiveStream

        nowPlayingInfo.remove(MPMediaItemPropertyPlaybackDuration)
        nowPlayingInfo.remove(MPNowPlayingInfoPropertyElapsedPlaybackTime)
        updateNowPlayingInfo()

        imageUrl?.let {
            artworkLoadJob = CoroutineScope(Dispatchers.Main).launch {
                runCatching {
                    val img = artworkLoader.loadArtwork(imageUrl) ?: return@launch
                    ensureActive()
                    nowPlayingInfo[MPMediaItemPropertyArtwork] = MPMediaItemArtwork(img)
                    updateNowPlayingInfo()
                }
            }
        }
    }

    fun updatePlaybackDuration(duration: Float) {
        nowPlayingInfo[MPMediaItemPropertyPlaybackDuration] = duration
        updateNowPlayingInfo()
    }

    fun updatePlaybackRate(rate: Float) {
        nowPlayingInfo[MPNowPlayingInfoPropertyPlaybackRate] = rate
        // provide info for CarPlay
        MPNowPlayingPlaybackStatePlaying
        nowPlayingInfoCenter.playbackState = if (rate > 0) {
            MPNowPlayingPlaybackStatePlaying
        } else {
            MPNowPlayingPlaybackStatePaused
        }
        updateNowPlayingInfo()
    }

    fun updateElapsedPlaybackTime(time: Float) {
        nowPlayingInfo[MPNowPlayingInfoPropertyElapsedPlaybackTime] = time
        updateNowPlayingInfo()
    }

    fun setItemsCountInfo(currentIndex: Int, totalCount: Int) {
        nowPlayingInfo[MPNowPlayingInfoPropertyPlaybackQueueIndex] = currentIndex
        nowPlayingInfo[MPNowPlayingInfoPropertyPlaybackQueueCount] = totalCount
        updateNowPlayingInfo()
    }

    fun removeNowPlayingInfo() {
        nowPlayingInfo.clear()
        updateNowPlayingInfo()
    }

    private fun updateNowPlayingInfo() {
        nowPlayingInfoCenter.nowPlayingInfo = nowPlayingInfo
    }

}