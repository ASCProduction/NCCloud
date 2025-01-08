package tk.shkabaj.android.shkabaj.widget.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.java.KoinJavaComponent.inject
import tk.shkabaj.android.shkabaj.extensions.toJsonString
import tk.shkabaj.android.shkabaj.network.NCCloudApiService
import tk.shkabaj.android.shkabaj.ui.news.model.NewsItemModel
import tk.shkabaj.android.shkabaj.widget.NewsWidget
import tk.shkabaj.android.shkabaj.widget.model.NewsWidgetModel

class NewsWidgetWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {

    private val apiService: NCCloudApiService by inject(NCCloudApiService::class.java)

    override suspend fun doWork(): Result {
        val response = apiService.getNews()
        val news = response.map { item ->
            NewsItemModel(
                entity = item,
                isBookmarked = false
            )
        }
        val answerNews = news.filter { it.entity.ioswidget }
        val updatedNewsList = answerNews.map { newsItem ->
            NewsWidgetModel(
                title = newsItem.entity.title ?: "",
                imagePath = newsItem.entity.fullImageUrl ?: ""
            )
        }
        Log.i("NewsWidget", answerNews.toString())

        val prefs = applicationContext.getSharedPreferences("widget_news", Context.MODE_PRIVATE)
        val updateNewsJson = updatedNewsList.toJsonString()
        prefs.edit().putString("news_data", updateNewsJson).apply()
        Log.i("NewsWidget", "Saved data: $updateNewsJson")

        NewsWidget.update(applicationContext)
        Log.i("NewsWidget", "Updating news widget")
        return Result.success()
    }
}