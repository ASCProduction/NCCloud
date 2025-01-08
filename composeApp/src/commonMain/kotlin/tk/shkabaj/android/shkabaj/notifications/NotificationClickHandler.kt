package tk.shkabaj.android.shkabaj.notifications

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tk.shkabaj.android.shkabaj.navigation.tabs.TabItem

class NotificationClickHandler(
    private val navigationHelper: NavigationHelper
) {

    enum class Type(val value: String) {
        LINK("link"),
        MAIN_SCREEN("mainScreen"),
        START_RADIO("startRadio"),
        TV_PLAY("TVPlay"),
        LAJME_SCREEN("Lajme"),
        VIDEO_SCREEN("VideoScreen"),
        MOTI_SCREEN("MotiScreen"),
        RADIO_SCREEN("radioScreen"),
        POPUP_SCREEN("POPUPScreen")
    }

    fun handleClick(data: Map<String, Any?>, title: String?, isAppActive: Boolean) {
        println("TEST: notification click with data $data and title $title")
        val typeString = data["type"] as? String ?: return
        val type = Type.entries.find { it.value.lowercase() == typeString.lowercase() } ?: return

        val alertMsg = title ?: ""

        when(type) {
            Type.LINK -> {
                val url = data["url"] as? String ?: return
                handlePushWithAlertMessage(alertMsg, isAppActive) {
                    navigationHelper.openLink(url, false)
                }
            }
            Type.MAIN_SCREEN -> {
                handlePushWithAlertMessage(alertMsg, isAppActive) {
                    navigationHelper.openTab(TabItem.HOME)
                }
            }
            Type.START_RADIO -> {
                val radioName = data["name"] as? String ?: return
                handlePushWithAlertMessage(alertMsg, isAppActive) {
                    navigationHelper.openRadio(radioName)
                }
            }
            Type.TV_PLAY -> {
                val tvName = data["name"] as? String ?: return
                handlePushWithAlertMessage(alertMsg, isAppActive) {
                    navigationHelper.openTV(tvName)
                }
            }
            Type.LAJME_SCREEN -> {
                val indexTab = (data["index"] as? String)?.toIntOrNull() ?: return
                handlePushWithAlertMessage(alertMsg, isAppActive) {
                    navigationHelper.openLajme(indexTab)
                }
            }
            Type.VIDEO_SCREEN -> {
                handlePushWithAlertMessage(alertMsg, isAppActive) {
                    //navigationHelper.openTab(TabItem.VIDEO)
                }
            }
            Type.MOTI_SCREEN -> {
                val indexTab = (data["index"] as? String)?.toIntOrNull() ?: return
                handlePushWithAlertMessage(alertMsg, isAppActive) {
                    navigationHelper.openWeather(indexTab)
                }
            }
            Type.RADIO_SCREEN -> {
                handlePushWithAlertMessage(alertMsg, isAppActive) {
                    //navigationHelper.openTab(TabItem.RADIO)
                }
            }
            Type.POPUP_SCREEN -> {
                val link = data["link"] as? String ?: return
                handlePushWithAlertMessage(alertMsg, isAppActive) {
                    navigationHelper.openLink(link, true)
                }
            }
        }
    }

    private fun handlePushWithAlertMessage(msg: String, isAppActive: Boolean, action: () -> Unit) {
        if (isAppActive) {
            navigationHelper.showPushDialog(msg, action)
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                delay(300)
                action()
            }
        }
    }

}

//Payload examples
/*
 {
 "type" : "link",
 "url" : "http://google.com",
 "alert" : "open a link on Safari",
 "sound" : "default"
 }

 {
 "type" : "mainScreen",
 "sound" : "default",
 "alert" : "open main screen"
 }

 {
 "type" : "startRadio",
 "sound" : "default",
 "alert" : "startRadio",
 "name" : "Radio Sprint"
 }

 {
 "type" : "TVPlay",
 "sound" : "default",
 "alert" : "TV Play",
 "name" : "RTV Ora News"
 }

 {
 "type" : "Lajme",
 "sound" : "default",
 "alert" : "Open Lajme",
 "index" : "2"
 }

 {
 "type" : "VideoScreen",
 "sound" : "default",
 "alert" : "Open Video screen",
 "index" : "2"
 }

 {
 "type" : "MotiScreen",
 "sound" : "default",
 "alert" : "Open Moti screen",
 "index" : "2"
 }

 {
 "type" : "radioScreen",
 "sound" : "default",
 "alert" : "Open Radio screen",
 }

 {
 "type" : "POPUPScreen",
 "sound" : "default",
 "alert" : "Open pop up screen",
 "link" : "https://www.youtube.com/watch?v=t3qDt8mS92g"
 }
 */