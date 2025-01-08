package tk.shkabaj.android.shkabaj.ui.news.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.ic_bookmark
import nccloud.composeapp.generated.resources.ic_bookmark_on
import nccloud.composeapp.generated.resources.ic_info
import nccloud.composeapp.generated.resources.ic_share
import tk.shkabaj.android.shkabaj.extensions.getErrorMessage
import tk.shkabaj.android.shkabaj.modules.news.MainNewsAction
import tk.shkabaj.android.shkabaj.modules.news.MainNewsEvent
import tk.shkabaj.android.shkabaj.modules.news.NewsMainViewModel
import tk.shkabaj.android.shkabaj.network.entity.news.main.NewsEntity
import tk.shkabaj.android.shkabaj.ui.customcomponents.NCCloudAsyncImage
import tk.shkabaj.android.shkabaj.ui.customcomponents.showSnackBar
import tk.shkabaj.android.shkabaj.ui.news.model.NewsItemModel
import tk.shkabaj.android.shkabaj.ui.theme.CulturedGreyColor
import tk.shkabaj.android.shkabaj.ui.theme.DividerNewsColor
import tk.shkabaj.android.shkabaj.ui.theme.GreyColor
import tk.shkabaj.android.shkabaj.ui.theme.MaastrichtBlueColor
import tk.shkabaj.android.shkabaj.ui.theme.MainBGColor
import tk.shkabaj.android.shkabaj.ui.theme.OldSilverColor
import tk.shkabaj.android.shkabaj.ui.theme.TextBGColor
import tk.shkabaj.android.shkabaj.ui.theme.ToolbarIconColor
import tk.shkabaj.android.shkabaj.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsList(viewModel: NewsMainViewModel) {
    val state by viewModel.viewStates().collectAsState()
    val newsListItems = state.newsList
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.viewActions().collect { uiAction ->
            when (uiAction) {
                is MainNewsAction.ShowError -> showSnackBar(
                    message = uiAction.error.getErrorMessage(),
                    snackBarHostState = snackBarHostState,
                    coroutine = coroutineScope
                )
                else -> {}
            }
        }
    }

    val onBookmarkClick: (NewsEntity) -> Unit = { item ->
        viewModel.obtainEvent(MainNewsEvent.OnBookmarkClick(item))
    }

    val onShareClick: (NewsEntity) -> Unit = { item ->
        viewModel.obtainEvent(event = MainNewsEvent.OnShareClick(item))
    }

    val onOpenUrl: (url: String) -> Unit = { url ->
        viewModel.obtainEvent(event = MainNewsEvent.OnOpenUrl(url))
    }

    PullToRefreshBox(
        isRefreshing = state.progress,
        onRefresh = { viewModel.obtainEvent(MainNewsEvent.OnRefresh) },
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxSize(),
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().background(color = MainBGColor),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(newsListItems) { newsItem ->
                    NewsItem(newsItem, onBookmarkClick, onShareClick, onOpenUrl)
                }
            }
        }
    }
}

@Composable
private fun NewsItem(
    item: NewsItemModel,
    onBookmarkClick: (NewsEntity) -> Unit,
    onShareClick: (NewsEntity) -> Unit,
    onOpenUrl: (url: String) -> Unit
) {
    val newsItem = item.entity

    val onItemClicked: (String, String) -> Unit = { title, url ->
        onOpenUrl(url)
    }

    var textExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(CulturedGreyColor)
            .clickable {
                newsItem.articleUrl?.let { onOpenUrl(it) }
            },
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
        ) {
            NCCloudAsyncImage(
                model = newsItem.fullImageUrl,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            if (newsItem.imageCredits[0] != "....") {
                Box(
                    modifier = Modifier.align(alignment = Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 4.dp)
                        .background(color = TextBGColor, shape = RoundedCornerShape(4.dp))
                        .clickable { textExpanded = !textExpanded }
                ) {
                    if (textExpanded) {
                        Text(
                            modifier = Modifier
                                .widthIn(0.dp, 230.dp)
                                .padding(all = 4.dp),
                            maxLines = 2,
                            lineHeight = 13.sp,
                            overflow = TextOverflow.Ellipsis,
                            color = MaastrichtBlueColor,
                            fontSize = 13.sp,
                            text = newsItem.imageCredits[0]
                        )
                    } else {
                        Image(
                            painter = painterResource(resource = Res.drawable.ic_info),
                            contentDescription = null,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            colorFilter = ColorFilter.tint(ToolbarIconColor)
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                NCCloudAsyncImage(
                    model = newsItem.fullSourceImageUrl,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                )

                Text(
                    text = newsItem.sourceName ?: "",
                    style = Typography.headlineLarge.copy(
                        color = OldSilverColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
                Spacer(modifier = Modifier.weight(weight = 1f))
                Image(
                    modifier = Modifier.size(24.dp).clickable { onBookmarkClick(newsItem) },
                    painter = painterResource(
                        if (item.isBookmarked) {
                            Res.drawable.ic_bookmark_on
                        } else {
                            Res.drawable.ic_bookmark
                        }
                    ),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = MaastrichtBlueColor)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    modifier = Modifier.size(24.dp).clickable { onShareClick(newsItem) },
                    painter = painterResource(Res.drawable.ic_share),
                    contentDescription = null,
                    tint = MaastrichtBlueColor
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.Top)
                        .padding(bottom = if (newsItem.webTooltip != null || newsItem.videoTooltip != null) 8.dp else 0.dp),
                    text = newsItem.title ?: "",
                    maxLines = 2,
                    style = Typography.headlineLarge.copy(
                        color = MaastrichtBlueColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
        Column (
            modifier = Modifier
                .padding(bottom = 12.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (newsItem.webTooltip != null) {
                DopNews(
                    text = newsItem.webTooltip,
                    url = newsItem.webUrl ?: "",
                    onItemClicked = onItemClicked
                )
            }
            if (newsItem.videoTooltip != null) {
                DopNews(
                    text = newsItem.videoTooltip,
                    url = newsItem.videoUrl ?: "",
                    onItemClicked = onItemClicked
                )
            }
        }
    }
}

@Composable
private fun DopNews(
    text: String,
    url: String,
    onItemClicked: (String, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        HorizontalDivider(
            color = DividerNewsColor,
            thickness = 0.5.dp
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClicked(text, url) },
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = text,
                maxLines = 2,
                style = Typography.headlineLarge.copy(
                    color = GreyColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}