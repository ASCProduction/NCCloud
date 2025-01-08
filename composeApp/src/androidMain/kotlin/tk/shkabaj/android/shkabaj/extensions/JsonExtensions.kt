package tk.shkabaj.android.shkabaj.extensions

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val json = Json { ignoreUnknownKeys = true }

inline fun <reified T> String.parseJson(): T {
    return json.decodeFromString(this)
}

inline fun <reified T> T.toJsonString(): String {
    return json.encodeToString(value = this)
}