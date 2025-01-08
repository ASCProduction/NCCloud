package tk.shkabaj.android.shkabaj.utils

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import tk.shkabaj.android.shkabaj.NCCloudApp
import tk.shkabaj.android.shkabaj.utils.analytics.AnalyticsEvent
import tk.shkabaj.android.shkabaj.utils.analytics.AnalyticsScreen
import tk.shkabaj.android.shkabaj.utils.analytics.PlatformAnalyticsTracker

internal class AndroidAnalyticsTracker: PlatformAnalyticsTracker {

    private val firebaseAnalytics = FirebaseAnalytics.getInstance(NCCloudApp.context)

    override fun trackEvent(event: AnalyticsEvent) {
        val params = Bundle().apply {
            putString(event.eventName, event.value)
        }
        firebaseAnalytics.logEvent("shkabaj_event", params)
    }

    override fun trackScreen(screen: AnalyticsScreen) {
        val params = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_NAME, screen.screenName)
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params)
    }

}