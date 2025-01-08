package tk.shkabaj.android.shkabaj.data.entity

import androidx.room.Entity
import tk.shkabaj.android.shkabaj.network.entity.news.main.NewsEntity

@Entity(
    tableName = "news",
    primaryKeys = ["articleUrl"]
)
data class NewsDatabaseEntity(
    val title: String,
    val articleUrl: String,
    val description: String,
    val sourceImageUrl: String? = null,
    val imageUrl: String? = null,
    val sourceName: String? = null,
    val timeUpdated: String? = null,
    val webTooltip: String? = null,
    val webUrl: String? = null,
    val photoTooltip: String? = null,
    val photoUrl: String? = null,
    val videoTooltip: String? = null,
    val videoUrl: String? = null,
    val bookmarkedDate: Double? = null
)

fun NewsDatabaseEntity.toDomainEntity(): NewsEntity {
    return NewsEntity(
        title = title,
        articleUrl = articleUrl,
        description = description,
        imageUrl = imageUrl,
        sourceImageUrl = sourceImageUrl,
        webUrl = webUrl,
        webTooltip = webTooltip,
        photoUrl = photoUrl,
        photoTooltip = photoTooltip,
        videoUrl = videoUrl,
        videoTooltip = videoTooltip,
        timeUpdated = timeUpdated
    )
}
