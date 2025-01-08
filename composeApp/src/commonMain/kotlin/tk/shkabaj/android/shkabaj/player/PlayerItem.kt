package tk.shkabaj.android.shkabaj.player


sealed class PlayerItem {
    abstract val imageUrl: String?
    abstract val title: String?
    abstract val subtitle: String?
    abstract val subtitle3: String?
    abstract val listeningInfo: String?
    abstract val site: String?

    data class Podcast(val podcastImage: String?,
                       val podcastTitle: String?,
                       val podcastSubtitle: String?): PlayerItem() {
        override val imageUrl: String? = podcastImage
        override val title: String? = podcastTitle
        override val subtitle: String? = podcastSubtitle
        override val subtitle3: String? = null
        override val listeningInfo: String? = null
        override val site: String? = null
    }

    val id: String
        get() = ""
}