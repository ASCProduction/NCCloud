package tk.shkabaj.android.shkabaj.ui.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.TopNews
import tk.shkabaj.android.shkabaj.ui.customcomponents.NCCloudAsyncImage
import tk.shkabaj.android.shkabaj.ui.news.model.NewsItemModel
import tk.shkabaj.android.shkabaj.ui.theme.CulturedGreyColor
import tk.shkabaj.android.shkabaj.ui.theme.MaastrichtBlueColor
import tk.shkabaj.android.shkabaj.ui.theme.SFProText

@Composable
fun NewsCard(
    news: List<NewsItemModel>,
    onItemClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().background(CulturedGreyColor).padding(bottom = 16.dp)
    ) {
        CardHeader(title = stringResource(Res.string.TopNews), showMore = false)

        NewsPager(
            news = news,
            onItemClick = onItemClick,
        )
    }
}

@Composable
private fun NewsPager(
    news: List<NewsItemModel>,
    onItemClick: (String) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { news.size })
    var isUserScrolling by remember { mutableStateOf(false) }
    var isAutoScroll by remember { mutableStateOf(false) }

    LaunchedEffect(isUserScrolling) {
        if (!isUserScrolling) {
            while (true) {
                delay(3000)
                isAutoScroll = true
                val nextPage = (pagerState.currentPage + 1) % news.size
                pagerState.animateScrollToPage(nextPage)
                isAutoScroll = false
            }
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        if (!isAutoScroll) {
            isUserScrolling = true
            delay(1500)
            isUserScrolling = false
        }
    }

    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 16.dp),
        pageSpacing = 8.dp
    ) { page ->
        val feed = news[page]

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .clickable { feed.entity.articleUrl?.let { onItemClick(it) } }
        ) {
            NCCloudAsyncImage(
                modifier = Modifier.fillMaxWidth().height(height = 200.dp)
                    .clip(RoundedCornerShape(4.dp)),
                model = feed.entity.fullImageUrl,
                contentScale = ContentScale.Crop,
            )
            Text(
                modifier = Modifier
                    .padding(top = 8.dp, start = 2.dp, end = 24.dp)
                    .height(height = 72.dp),
                text = feed.entity.title ?: "",
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = MaastrichtBlueColor,
                fontFamily = SFProText,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            )
        }
    }
}