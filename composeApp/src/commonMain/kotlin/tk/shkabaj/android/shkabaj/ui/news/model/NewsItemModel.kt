package tk.shkabaj.android.shkabaj.ui.news.model

import tk.shkabaj.android.shkabaj.network.entity.news.main.NewsEntity

data class NewsItemModel(
    val entity: NewsEntity,
    val isBookmarked: Boolean,
    val bookmarkedDate: Double? = null
)
