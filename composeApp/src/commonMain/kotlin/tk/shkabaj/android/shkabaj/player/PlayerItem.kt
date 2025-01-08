package tk.shkabaj.android.shkabaj.player

import tk.shkabaj.android.shkabaj.ui.news.model.PodcastItemUIModel

sealed class PlayerItem {
    abstract val mediaUrl: String
    abstract val imageUrl: String?
    abstract val title: String?
    abstract val subtitle: String?
    abstract val subtitle2: String?
    abstract val subtitle3: String?
    abstract val listeningInfo: String?
    abstract val site: String?

    data class Podcast(val podcastItem: PodcastItemUIModel,
                       val podcastImage: String?,
                       val podcastTitle: String?,
                       val podcastSubtitle: String?): PlayerItem() {
        override val mediaUrl: String = podcastItem.streamUrl ?: ""
        override val imageUrl: String? = podcastImage
        override val title: String? = podcastTitle
        override val subtitle: String? = podcastSubtitle
        override val subtitle2: String? = podcastItem.title
        override val subtitle3: String? = null
        override val listeningInfo: String? = null
        override val site: String? = null
    }

    val id: String
        get() = when(this) {
            is Podcast -> mediaUrl
        }
}