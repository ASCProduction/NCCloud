package tk.shkabaj.android.shkabaj.data.entity

import androidx.room.Entity
import kotlinx.serialization.json.Json
import tk.shkabaj.android.shkabaj.modules.main.model.NewsUpdateModel

@Entity(
    tableName = "updateInfo",
    primaryKeys = ["updateType"]
)
data class UpdateDatabaseEntity (
    val updateType: String,
    val updateInfoJson: String
)

fun String.toNewsUpdateModelList(): List<NewsUpdateModel> {
    return Json.decodeFromString(this)
}