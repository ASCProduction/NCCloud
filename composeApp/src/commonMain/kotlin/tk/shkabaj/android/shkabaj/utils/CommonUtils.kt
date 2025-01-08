package tk.shkabaj.android.shkabaj.utils

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.allDrawableResources
import tk.shkabaj.android.shkabaj.network.NetworkConfig

object CommonUtils {

    @OptIn(ExperimentalResourceApi::class)
    fun getDrawableByName(name: String): DrawableResource? {
        return Res.allDrawableResources.entries.firstOrNull { it.key == name }?.value
    }

    fun getIconNameFromUrl(url: String): String {
        val components = url.split("/")
        return if (components.isNotEmpty() && components.size > 1) {
            val name = "w" + components.last().lowercase()
            name.substringBefore(delimiter = ".")
        } else {
            url
        }
    }

    fun getImageUrlFromPath(path: String): String {
        val pathSubstring = when {
            path.startsWith("../") -> path.substring(3)
            path.startsWith("/") -> path.substring(1)
            else -> ""
        }
        return "${NetworkConfig.BASE_URL}$pathSubstring"
    }

    fun safeString(index: Int, components: List<String>): String {
        return if (index < components.size) components[index] else ""
    }

}