package tk.shkabaj.android.shkabaj.widget

import android.content.Context
import android.util.Log
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import tk.shkabaj.android.shkabaj.extensions.parseJson
import tk.shkabaj.android.shkabaj.widget.content.WidgetContent
import tk.shkabaj.android.shkabaj.widget.model.NewsWidgetModel

object NewsWidget: GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val newsList = loadNews(context)
        provideContent {
            context.WidgetContent(newsList)
        }
    }

    suspend fun update(context: Context) {
        GlanceAppWidgetManager(context).getGlanceIds(NewsWidget::class.java).forEach { id ->
            update(context, id)
        }
    }

    private fun loadNews(context: Context): List<NewsWidgetModel> {
        Log.i("NewsWidget", "Start loading news info")
        val prefs = context.getSharedPreferences("widget_news", Context.MODE_PRIVATE)
        val newsJson = prefs.getString("news_data", null)
        Log.i("NewsWidget", "NewsJson: $newsJson")
        return newsJson?.parseJson<List<NewsWidgetModel>>() ?: emptyList()
    }
}

class NewsWidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = NewsWidget
}