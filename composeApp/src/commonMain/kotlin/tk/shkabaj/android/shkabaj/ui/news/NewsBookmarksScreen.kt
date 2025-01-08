package tk.shkabaj.android.shkabaj.ui.news

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import nccloud.composeapp.generated.resources.FavouriteEmptyTextPlaceHolder
import nccloud.composeapp.generated.resources.NewsBookmarksTitle
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.ic_bookmark
import nccloud.composeapp.generated.resources.ic_bookmark_on
import tk.shkabaj.android.shkabaj.extensions.getErrorMessage
import tk.shkabaj.android.shkabaj.extensions.injectScreenModel
import tk.shkabaj.android.shkabaj.modules.news.NewsBookmarksEvent
import tk.shkabaj.android.shkabaj.modules.news.NewsBookmarksViewModel
import tk.shkabaj.android.shkabaj.network.entity.news.main.NewsEntity
import tk.shkabaj.android.shkabaj.ui.news.model.NewsItemModel
import tk.shkabaj.android.shkabaj.ui.theme.Afacad
import tk.shkabaj.android.shkabaj.ui.theme.DividerNewsColor
import tk.shkabaj.android.shkabaj.ui.theme.MaastrichtBlueColor
import tk.shkabaj.android.shkabaj.ui.theme.MainBGColor
import tk.shkabaj.android.shkabaj.ui.theme.OldSilverColor
import tk.shkabaj.android.shkabaj.modules.news.NewsBookmarksAction
import tk.shkabaj.android.shkabaj.ui.customcomponents.showSnackBar
import tk.shkabaj.android.shkabaj.navigation.AppScreen
import tk.shkabaj.android.shkabaj.navigation.AppScreenTitle
import tk.shkabaj.android.shkabaj.ui.customcomponents.NCCloudAsyncImage
import tk.shkabaj.android.shkabaj.ui.toolbar.ToolbarAction
import tk.shkabaj.android.shkabaj.utils.DateTimeUtils

class NewsBookmarksScreen : AppScreen {

    override val title = AppScreenTitle.Res(Res.string.NewsBookmarksTitle)
    override val toolbarActions: List<ToolbarAction>
        get() = emptyList()

    @Composable
    override fun Content() {
        val viewModel = injectScreenModel<NewsBookmarksViewModel>()
        val state by viewModel.viewStates().collectAsState()
        val snackBarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            handleToolbarAction { toolbarAction ->
                viewModel.obtainEvent(event = NewsBookmarksEvent.OnToolbarEvent(toolbarAction = toolbarAction))
            }
        }

        LaunchedEffect(Unit) {
            viewModel.viewActions().collect { uiAction ->
                when(uiAction) {
                    is NewsBookmarksAction.ShowError -> showSnackBar(
                        message = uiAction.error.getErrorMessage(),
                        snackBarHostState = snackBarHostState,
                        coroutine = coroutineScope
                    )
                    is NewsBookmarksAction.BackNavigation -> navigator.pop()
                    else -> {}
                }
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(color = MainBGColor)
            ) {
                if (state.bookmarks.isEmpty()) {
                    Text(
                        modifier = Modifier.padding(horizontal = 32.dp)
                            .align(alignment = Alignment.Center),
                        text = stringResource(resource = Res.string.FavouriteEmptyTextPlaceHolder),
                        style = TextStyle(
                            fontFamily = Afacad,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 22.sp,
                            color = MaastrichtBlueColor,
                            textAlign = TextAlign.Center
                        )
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(
                            state.bookmarks,
                            key = { it.entity.articleUrl ?: "" }
                        ) { bookmark ->
                            NewsBookmarkItem(
                                item = bookmark,
                                onBookmarkClick = { entity ->
                                    viewModel.obtainEvent(NewsBookmarksEvent.OnBookmarkClick(entity))
                                },
                                onItemClick = { entity ->
                                    viewModel.obtainEvent(event = NewsBookmarksEvent.OnOpenUrl(entity))
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun NewsBookmarkItem(
        item: NewsItemModel,
        onBookmarkClick: (NewsEntity) -> Unit,
        onItemClick: (NewsEntity) -> Unit
    ) {
        val entity = item.entity

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(107.dp)
                .background(Color.Transparent)
                .clickable { onItemClick(item.entity) }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(94.dp)
                    .background(Color.Transparent, shape = RoundedCornerShape(8.dp))
                    .padding(bottom = 12.dp)
            ) {
                entity.fullImageUrl?.let {
                    NCCloudAsyncImage(
                        model = it,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(width = 94.dp)
                            .height(94.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                    )
                }
                Column(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 2.dp)
                    ) {
                        Text(
                            text = entity.title ?: "",
                            fontSize = 17.sp,
                            color = MaastrichtBlueColor,
                            maxLines = 3,
                            modifier = Modifier.weight(1f).padding(start = 8.dp, end = 16.dp)
                        )
                        Image(
                            painter = painterResource(
                                resource = if (item.isBookmarked)
                                    Res.drawable.ic_bookmark_on
                                else
                                    Res.drawable.ic_bookmark
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { onBookmarkClick(entity) },
                            colorFilter = ColorFilter.tint(
                                color = MaastrichtBlueColor
                            )
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = item.bookmarkedDate?.let { DateTimeUtils.dateStringFromTimestamp(it) } ?: "",
                            color = OldSilverColor,
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp).align(
                                alignment = Alignment.CenterVertically
                            )
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = DividerNewsColor)
            )
        }
    }

}