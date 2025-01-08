package tk.shkabaj.android.shkabaj.player

enum class AudioPlayerState {
    PLAY, STOP, BUFFERING
}

sealed class AudioPlayerEvent {
    data class StateUpdated(val state: AudioPlayerState): AudioPlayerEvent()
    data class StartPlaying(val item: PlayerItem): AudioPlayerEvent()
    data class UpdateTime(val current: Long, val duration: Long, val progress: Float): AudioPlayerEvent()
}

expect class AudioPlayer() {

    var eventsHandler: ((AudioPlayerEvent) -> Unit)?
    var onError: ((String?) -> Unit)?

    fun play()
    fun pause()
    fun next()
    fun prev()
    fun seekTo(pos: Long)
    fun seekToPercent(percent: Float)

    fun play(items: List<PlayerItem>, startIndex: Int)
    fun cleanUp()

}