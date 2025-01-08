package tk.shkabaj.android.shkabaj.widget.model

import kotlinx.serialization.Serializable

@Serializable
data class NewsWidgetModel (
    val title: String,
    val imagePath: String
)