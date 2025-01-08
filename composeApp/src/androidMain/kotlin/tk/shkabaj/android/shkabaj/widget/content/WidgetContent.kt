package tk.shkabaj.android.shkabaj.widget.content

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import androidx.glance.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.layout.Row
import androidx.glance.GlanceModifier
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.layout.Alignment
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import tk.shkabaj.android.shkabaj.MainActivity
import tk.shkabaj.android.shkabaj.R
import tk.shkabaj.android.shkabaj.widget.manager.ImageLoaderManager
import tk.shkabaj.android.shkabaj.widget.model.NewsWidgetModel
import tk.shkabaj.android.shkabaj.widget.work.scheduleWidgetUpdate

@SuppressLint("RestrictedApi")
@Composable
fun Context.WidgetContent(newsList: List<NewsWidgetModel>) {
    val textColor = ColorProvider(R.color.widget_text_color)
    val loader = remember {
        ImageLoaderManager(this@WidgetContent)
    }

    Column(
        modifier = GlanceModifier.fillMaxSize()
            .background(color = R.color.widget_background)
    ) {
        Row(
            modifier = GlanceModifier.fillMaxWidth().height(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = GlanceModifier.height(28.dp).width(44.dp)
                    .padding(start = 16.dp),
                provider = ImageProvider(R.drawable.ic_logo_widget),
                contentDescription = null
            )
            Text(
                modifier = GlanceModifier.padding(start = 16.dp),
                text = "Shkabaj - Lajme",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = textColor
                )
            )
            Spacer(modifier = GlanceModifier.defaultWeight())
            Image(
                modifier = GlanceModifier.height(28.dp).width(44.dp)
                    .padding(end = 16.dp)
                    .clickable {
                        scheduleWidgetUpdate(this@WidgetContent)
                    },
                provider = ImageProvider(R.drawable.ic_reload),
                contentDescription = null
            )
        }

        Box(modifier = GlanceModifier
            .height(height = 1.dp)
            .fillMaxWidth()
            .background(Color(212, 212, 212))) {}

        LazyColumn(modifier = GlanceModifier.fillMaxSize()) {
            items(newsList) { item ->
                NewsItem(title = item.title, image = item.imagePath, loader)
            }
        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
private fun NewsItem(title: String, image: String, loader: ImageLoaderManager) {
    val bitmapImageState = remember {
        mutableStateOf<Bitmap?>(null)
    }

    LaunchedEffect(image) {
        bitmapImageState.value = loader.loadImageAsBitmap(image)
    }

    Column {
        Row(modifier = GlanceModifier.fillMaxWidth().height(height = 84.dp)
            .clickable(onClick = actionStartActivity<MainActivity>())) { //TODO: Navigate to news tab
            Image(
                modifier = GlanceModifier.height(80.dp).width(145.dp)
                    .padding(top = 4.dp, start = 16.dp),
                provider = if(bitmapImageState.value != null) {
                    ImageProvider(bitmapImageState.value!!)
                } else {
                    ImageProvider(R.drawable.ic_widget_news_placeholder)
                },
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Text(
                modifier = GlanceModifier.padding(top = 10.dp, start = 8.dp, end = 16.dp)
                    .defaultWeight(),
                text = title,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = ColorProvider(R.color.widget_text_color)
                ),
                maxLines = 3,
            )
        }
        Box(modifier = GlanceModifier
            .height(height = 1.dp)
            .fillMaxWidth()
            .background(Color(212, 212, 212))) {}
    }
}