package tk.shkabaj.android.shkabaj.network.utils

import nl.adaptivity.xmlutil.XmlDeclMode
import nl.adaptivity.xmlutil.core.XmlVersion
import nl.adaptivity.xmlutil.serialization.XML
import tk.shkabaj.android.shkabaj.network.entity.news.main.NewsListEntity

object XmlParser {

    private val xml: XML by lazy {
        XML {
            xmlVersion = XmlVersion.XML10
            xmlDeclMode = XmlDeclMode.Auto
            repairNamespaces = true
            defaultPolicy {
                pedantic = false
                ignoreUnknownChildren()
            }
        }
    }

    fun parseNews(body: String): NewsListEntity {
        return xml.decodeFromString(
            deserializer = NewsListEntity.serializer(),
            string = body
        )

    }

}