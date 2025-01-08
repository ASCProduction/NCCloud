package tk.shkabaj.android.shkabaj.player

interface ChromeCastHelper {

    sealed class Event {
        data object StartedCastSession: Event()
        data object EndedCastSession: Event()
        data object RemotePlayerPlay: Event()
        data object RemotePlayerPause: Event()
    }

    var eventsHandler: ((Event) -> Unit)?

    fun isCastConnected(): Boolean
    fun endCastSession()

    fun castItem(item: PlayerItem, startImmediately: Boolean)
    fun play()
    fun pause()
    fun stop()

}