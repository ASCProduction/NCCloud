package tk.shkabaj.android.shkabaj

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import tk.shkabaj.android.shkabaj.di.initAndroidCoin
import tk.shkabaj.android.shkabaj.widget.work.NewsWidgetWorker
import java.util.concurrent.TimeUnit

class NCCloudApp : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        initAndroidCoin(this)

        val periodicRequest = PeriodicWorkRequestBuilder<NewsWidgetWorker>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "NewsWidgetUpdate",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicRequest
        )
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
    }

}