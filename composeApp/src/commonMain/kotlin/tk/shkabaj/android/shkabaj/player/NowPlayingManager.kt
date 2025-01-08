package tk.shkabaj.android.shkabaj.player

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import tk.shkabaj.android.shkabaj.network.NCCloudApiService

data class PlayerTimerParams(
    val startTime: Instant,
    val remainingTime: Long
)

data class NowPlayingState(
    val playerState: AudioPlayerState = AudioPlayerState.STOP,
    val items: List<PlayerItem> = emptyList(),
    val currentItem: PlayerItem? = null,
    val currentIndex: Int? = null,
    val currentTime: Long? = null,
    val duration: Long? = null,
    val progress: Float? = null,
    val timer: PlayerTimerParams? = null
)

class NowPlayingManager(
    private val player: AudioPlayer,
    private val apiService: NCCloudApiService,
    private val chromeCastHelper: ChromeCastHelper
) {

    private val _state = MutableStateFlow(NowPlayingState())
    val state: StateFlow<NowPlayingState> = _state.asStateFlow()

    var onError: (() -> (Unit))? = null

    private var sleepTimerJob: Job? = null

    init {
        player.onError = { msg ->
            println("NowPlayingManager error $msg")
            onError?.invoke()
        }
        player.eventsHandler = { handlePlayerEvent(it) }
        chromeCastHelper.eventsHandler = { handleChromeCastEvent(it) }
    }

    fun stopSleepTimer() {
        sleepTimerJob?.cancel()
        _state.update { it.copy(timer = null) }
    }

    fun play(items: List<PlayerItem>, startIndex: Int) {
        val isPodcasts = items.any { it is PlayerItem.Podcast }
        if (isPodcasts) chromeCastHelper.endCastSession()

        val currentItem = items.getOrNull(startIndex)
        _state.update {
            it.copy(items = items, currentItem = currentItem, currentIndex = startIndex)
        }
        if (chromeCastHelper.isCastConnected()) {
            currentItem?.let { chromeCastHelper.castItem(it, true) }
        } else {
            player.play(items, startIndex)
        }
    }

    fun prev() {
        if (chromeCastHelper.isCastConnected()) {
            val prevIdx = (state.value.currentIndex ?: 1) - 1
            val prev = state.value.items.getOrNull(prevIdx) ?: return
            _state.update {
                it.copy(currentIndex = prevIdx, currentItem = prev)
            }
            chromeCastHelper.castItem(prev, true)
        } else {
            player.prev()
        }
    }

    fun next() {
        if (chromeCastHelper.isCastConnected()) {
            val nextIdx = (state.value.currentIndex ?: 0) + 1
            val next = state.value.items.getOrNull(nextIdx) ?: return
            _state.update {
                it.copy(currentIndex = nextIdx, currentItem = next)
            }
            chromeCastHelper.castItem(next, true)
        } else {
            player.next()
        }
    }

    fun pause() {
        if (chromeCastHelper.isCastConnected()) {
            chromeCastHelper.pause()
        } else {
            player.pause()
        }
    }

    fun play() {
        if (chromeCastHelper.isCastConnected()) {
            chromeCastHelper.play()
        } else {
            player.play()
        }
    }

    fun seekTo(pos: Long) {
        if (chromeCastHelper.isCastConnected()) return
        player.seekTo(pos)
    }

    fun seekToPercent(percent: Float) {
        if (chromeCastHelper.isCastConnected()) return
        player.seekToPercent(percent)
    }

    fun cleanUp() {
        player.cleanUp()
    }

    private fun handleChromeCastEvent(event: ChromeCastHelper.Event) {
        when(event) {
            ChromeCastHelper.Event.RemotePlayerPause -> {
                _state.update { it.copy(playerState = AudioPlayerState.STOP) }
            }
            ChromeCastHelper.Event.RemotePlayerPlay -> {
                _state.update { it.copy(playerState = AudioPlayerState.PLAY) }
            }
            ChromeCastHelper.Event.StartedCastSession -> {
                val item = state.value.currentItem ?: return
                val isNeedAutoplay = state.value.playerState == AudioPlayerState.PLAY
                stopSleepTimer()
                // pause local player
                player.pause()
                chromeCastHelper.castItem(item, isNeedAutoplay)
            }
            ChromeCastHelper.Event.EndedCastSession -> {
                // continue play locally if needed
                val idx = state.value.currentIndex ?: return
                play(state.value.items, idx)
            }
        }
    }

    private fun handlePlayerEvent(event: AudioPlayerEvent) {
        when(event) {
            is AudioPlayerEvent.StartPlaying -> {
                if (state.value.currentItem != event.item) {
                    onStartPlayingItem(event.item)
                }
                _state.update {
                    it.copy(
                        currentItem = event.item,
                        currentIndex = it.items.indexOf(event.item),
                        currentTime = null,
                        duration = null,
                        progress = null
                    )
                }
            }
            is AudioPlayerEvent.StateUpdated -> {
                _state.update { it.copy(playerState = event.state) }
            }
            is AudioPlayerEvent.UpdateTime -> {
                _state.update {
                    it.copy(
                        currentTime = event.current,
                        duration = event.duration,
                        progress = event.progress
                    )
                }
            }
        }
    }

    private fun onStartPlayingItem(item: PlayerItem) {

    }

    private fun trackListenRadio(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                apiService.listenRadio(id)
            } catch (e: Exception) {
                println("trackListenRadio error $e")
            }
        }
    }

}