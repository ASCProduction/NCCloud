package tk.shkabaj.android.shkabaj.migration.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tk.shkabaj.android.shkabaj.data.entity.NewsDatabaseEntity
import tk.shkabaj.android.shkabaj.extensions.parseDateStringToTimestamp

@Serializable
data class OldNewsFavouriteModel (
    val articleURI: String,
    val bookmarkedDate: String,
    val bookmarkedTime: Long,
    val description: String,
    val imageCredits: List<String>,
    val imageURLs: List<String>,
    @SerialName("ioswidget") val iosWidget: Boolean,
    val isSelected: Boolean,
    val sliderMobi: Boolean,
    @SerialName("slidshow") val slidShow: Boolean,
    val sourceImageURI: String,
    val sourceName: String,
    val title: String,
    val updatedTime: Double
)

fun OldNewsFavouriteModel.toDatabaseEntity(): NewsDatabaseEntity {
    return NewsDatabaseEntity(
        title = this.title,
        articleUrl = this.articleURI,
        description = this.description,
        sourceImageUrl = this.sourceImageURI,
        sourceName = this.sourceName,
        timeUpdated = this.updatedTime.toString(),
        bookmarkedDate = this.bookmarkedDate.parseDateStringToTimestamp(),
        imageUrl = this.imageURLs[0]
    )
}