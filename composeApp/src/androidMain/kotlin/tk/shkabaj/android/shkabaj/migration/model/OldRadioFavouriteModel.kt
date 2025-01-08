package tk.shkabaj.android.shkabaj.migration.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OldRadioFavouriteModel (
    val radios: List<RadioId>
)

@Serializable
data class RadioId(
    @SerialName("radio_id") val radioId: Long
)