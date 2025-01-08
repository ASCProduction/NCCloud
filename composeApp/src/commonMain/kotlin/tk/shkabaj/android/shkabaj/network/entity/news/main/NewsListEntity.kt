package tk.shkabaj.android.shkabaj.network.entity.news.main

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

@Serializable
@SerialName(value = "newsList")
data class NewsListEntity(
    @SerialName(value = "newsTimestamp")
    @XmlElement
    val newsTimestamp: String? = null,
    val newsList: List<NewsEntity> = emptyList()
)
