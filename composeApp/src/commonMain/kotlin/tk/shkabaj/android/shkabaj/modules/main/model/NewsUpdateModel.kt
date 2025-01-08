package tk.shkabaj.android.shkabaj.modules.main.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class NewsUpdateModel (
    val articleUrl: String,
    val newsName: String
)

fun List<NewsUpdateModel>.toJson(): String {
    return Json.encodeToString(this)
}