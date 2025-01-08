package tk.shkabaj.android.shkabaj.player

import android.content.Context
import android.os.Build
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.OptionsProvider
import com.google.android.gms.cast.framework.SessionProvider
import com.google.android.gms.cast.framework.media.CastMediaOptions
import com.google.android.gms.cast.framework.media.NotificationOptions

class ChromeCastProvider : OptionsProvider {

    companion object {
        const val RECEIVER_APPLICATION_ID = "F87CE75A"
    }

    override fun getCastOptions(context: Context): CastOptions {
        val notificationOptions = NotificationOptions.Builder()
            .setTargetActivityClassName(ExpandedControlsActivity::class.java.name)
            .setStopLiveStreamDrawableResId(com.google.android.gms.cast.framework.R.drawable.cast_ic_expanded_controller_pause)
            .build()

        val mediaOptionsBuilder = CastMediaOptions.Builder()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mediaOptionsBuilder.setMediaSessionEnabled(false)
            mediaOptionsBuilder.setNotificationOptions(null)
        } else {
            mediaOptionsBuilder.setNotificationOptions(notificationOptions)
        }

        mediaOptionsBuilder.setExpandedControllerActivityClassName(ExpandedControlsActivity::class.java.name)
        val mediaOptions = mediaOptionsBuilder.build()

        return CastOptions.Builder()
            .setReceiverApplicationId(RECEIVER_APPLICATION_ID)
            .setCastMediaOptions(mediaOptions)
            .build()
    }

    override fun getAdditionalSessionProviders(context: Context): List<SessionProvider>? {
        return null
    }
}