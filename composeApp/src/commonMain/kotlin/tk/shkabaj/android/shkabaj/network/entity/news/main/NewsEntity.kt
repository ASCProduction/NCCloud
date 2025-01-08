package tk.shkabaj.android.shkabaj.network.entity.news.main

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import tk.shkabaj.android.shkabaj.data.entity.NewsDatabaseEntity
import tk.shkabaj.android.shkabaj.network.NetworkConfig

@Serializable
@SerialName(value = "news")
data class NewsEntity(
    val sliderMobi: Boolean = false,
    val ioswidget: Boolean = false,
    @XmlElement
    @SerialName(value = "articleURI")
    val articleUrl: String? = null,
    @XmlElement
    @SerialName(value = "title")
    val title: String? = null,
    @XmlElement
    @SerialName(value = "imageURI")
    val imageUrl: String? = null,
    @XmlElement
    @SerialName(value = "description")
    val description: String? = null,
    @XmlElement
    @SerialName(value = "imageCredit")
    val imageCredits: List<String> = emptyList(),
    @XmlElement
    @SerialName(value = "sourceName")
    val sourceName: String? = null,
    @XmlElement
    @SerialName(value = "sourceImageURI")
    val sourceImageUrl: String? = null,
    @XmlElement
    @SerialName(value = "timeUpdated")
    val timeUpdated: String? = null,
    @XmlElement
    @SerialName(value = "webTooltip")
    val webTooltip: String? = null,
    @XmlElement
    @SerialName(value = "webURI")
    val webUrl: String? = null,
    @XmlElement
    @SerialName(value = "photoTooltip")
    val photoTooltip: String? = null,
    @XmlElement
    @SerialName(value = "photoURI")
    val photoUrl: String? = null,
    @XmlElement
    @SerialName(value = "videoTooltip")
    val videoTooltip: String? = null,
    @XmlElement
    @SerialName(value = "videoURI")
    val videoUrl: String? = null,
) {

    val fullSourceImageUrl: String
        get() = "${NetworkConfig.BASE_URL}$sourceImageUrl"

    val fullImageUrl: String?
        get() = imageUrl?.let {
            "${NetworkConfig.NEWS_IMAGE_BASE_URL}$it"
        }
}

fun NewsEntity.toDatabaseEntity(bookmarkedDate: Double? = null): NewsDatabaseEntity {
    return NewsDatabaseEntity(
        title = title ?: "",
        description = description ?: "",
        articleUrl = articleUrl ?: "",
        imageUrl = imageUrl,
        sourceImageUrl = sourceImageUrl,
        webUrl = webUrl,
        webTooltip = webTooltip,
        photoUrl = photoUrl,
        photoTooltip = photoTooltip,
        videoUrl = videoUrl,
        videoTooltip = videoTooltip,
        timeUpdated = timeUpdated,
        bookmarkedDate = bookmarkedDate
    )
}