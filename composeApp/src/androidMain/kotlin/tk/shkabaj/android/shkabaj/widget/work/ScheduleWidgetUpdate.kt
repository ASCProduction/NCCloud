package tk.shkabaj.android.shkabaj.widget.work

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

fun scheduleWidgetUpdate(context: Context) {
    val request = OneTimeWorkRequestBuilder<NewsWidgetWorker>().build()
    WorkManager.getInstance(context).enqueue(request)
}