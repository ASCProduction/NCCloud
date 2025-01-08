package tk.shkabaj.android.shkabaj.notification

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.Keep
import androidx.core.app.NotificationCompat
import com.onesignal.OneSignal
import com.onesignal.common.toMap
import com.onesignal.notifications.INotificationClickEvent
import com.onesignal.notifications.INotificationClickListener
import com.onesignal.notifications.INotificationLifecycleListener
import com.onesignal.notifications.INotificationReceivedEvent
import com.onesignal.notifications.INotificationServiceExtension
import com.onesignal.notifications.INotificationWillDisplayEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tk.shkabaj.android.shkabaj.R
import tk.shkabaj.android.shkabaj.notifications.NotificationClickHandler

@Keep
class OneSignalNotificationService(
    private val context: Context,
    private val pushClickHandler: NotificationClickHandler
): INotificationServiceExtension {

    private val appId = "be168aff-e8c9-4f31-89fa-36499632aec5"
    private var isActivityVisible = false

    fun initialize() {
        OneSignal.initWithContext(context, appId)
        requestPushPermission()

        OneSignal.Notifications.addForegroundLifecycleListener(object : INotificationLifecycleListener {
            override fun onWillDisplay(event: INotificationWillDisplayEvent) {
                Log.d("PUSH_TEST", "ON WILL DISPLAY: " + event.notification)
                val notification = event.notification
                if (isActivityVisible) {
                    val additionalData = notification.additionalData?.toMap()
                    additionalData?.toMap()?.let {
                        pushClickHandler.handleClick(it, notification.title, isActivityVisible)
                    }
                    event.preventDefault()
                }
            }
        })

        OneSignal.Notifications.addClickListener(object : INotificationClickListener {
            override fun onClick(event: INotificationClickEvent) {
                Log.d("PUSH_TEST", "ON CLICK: " + event.notification)
                val notification = event.notification
                val additionalData = notification.additionalData?.toMap()
                additionalData?.toMap()?.let {
                    pushClickHandler.handleClick(it, notification.title, isActivityVisible)
                }
            }
        })
    }

    fun changeStateActivity(activityVisible: Boolean) {
        isActivityVisible = activityVisible
    }

    @Override
    override fun onNotificationReceived(event: INotificationReceivedEvent) {
        val osNotification = event.notification
        Log.d("PUSH_TEST", "ON NOTIFICATION RECEIVED: " + osNotification.additionalData)
        if (isActivityVisible) {
            val additionalData = osNotification.additionalData?.toMap()
            additionalData?.toMap()?.let {
                pushClickHandler.handleClick(it, osNotification.title, isActivityVisible)
            }
        } else {
            osNotification.setExtender { builder: NotificationCompat.Builder ->
                builder
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            context.resources,
                            R.mipmap.ic_launcher_round
                        )
                    )
                    .setContentTitle(osNotification.title)
                    .setContentText(osNotification.body)
            }
        }
    }

    private fun requestPushPermission() {
        CoroutineScope(Dispatchers.IO).launch {
            OneSignal.Notifications.requestPermission(false)
        }
    }

}