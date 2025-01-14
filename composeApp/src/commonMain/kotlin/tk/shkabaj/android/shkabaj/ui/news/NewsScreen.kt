package tk.shkabaj.android.shkabaj.ui.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import nccloud.composeapp.generated.resources.NewsBottomSheetFilterTitle
import nccloud.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource
import tk.shkabaj.android.shkabaj.modules.news.NewsParentEvent
import tk.shkabaj.android.shkabaj.modules.news.NewsParentViewModel
import tk.shkabaj.android.shkabaj.extensions.injectScreenModel
import tk.shkabaj.android.shkabaj.modules.news.MainNewsEvent
import tk.shkabaj.android.shkabaj.modules.news.NewsMainViewModel
import tk.shkabaj.android.shkabaj.modules.news.NewsParentAction
import tk.shkabaj.android.shkabaj.ui.news.components.NewsList
import tk.shkabaj.android.shkabaj.ui.theme.MainBGColor
import tk.shkabaj.android.shkabaj.navigation.AppScreen
import tk.shkabaj.android.shkabaj.navigation.AppScreenTitle
import tk.shkabaj.android.shkabaj.ui.news.components.FilterBottomSheetLayout
import tk.shkabaj.android.shkabaj.ui.news.model.NewsFilterModel
import tk.shkabaj.android.shkabaj.ui.toolbar.ToolbarAction

class NewsScreen : AppScreen {

    override val title: AppScreenTitle? = null
    override val toolbarActions: List<ToolbarAction>
        get() = listOf(ToolbarAction.PLAYER, ToolbarAction.FAVOURITE, ToolbarAction.FILTER)

    @Composable
    override fun Content() {
        val viewModel = injectScreenModel<NewsParentViewModel>()
        val viewModelNews = injectScreenModel<NewsMainViewModel>()
        val newsState = viewModelNews.viewStates().collectAsState()

        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        val newsFilterBottomSheetTitle = stringResource(resource = Res.string.NewsBottomSheetFilterTitle)
        val newsFilters = NewsFilterModel.entries.toTypedArray()


        LaunchedEffect(Unit) {
            handleToolbarAction { toolbarAction ->
                viewModel.obtainEvent(event = NewsParentEvent.OnToolbarEvent(
                    toolbarAction = toolbarAction
                ))
            }
        }

        val closeBottomSheetEvent: (index: Int) -> Unit = {
            val selectedFilter = newsFilters[it]
            viewModelNews.obtainEvent(event = MainNewsEvent.OnChangeFilterType(newFilterType = selectedFilter))
            bottomSheetNavigator.hide()
        }

        val openBottomSheetEvent: () -> Unit = {
            val screen = FilterBottomSheetLayout(
                title = newsFilterBottomSheetTitle,
                items = newsFilters.map { it.title },
                startIndex = newsFilters.indexOf(newsState.value.currentFilterNewsType),
                closeSheet = closeBottomSheetEvent
            )
            bottomSheetNavigator.show(screen)
        }

        LaunchedEffect(Unit) {
            viewModel.viewActions().collect { uiAction ->
                when(uiAction) {
                    is NewsParentAction.NavigateToFavourite -> navigator.push(NewsBookmarksScreen())
                    is NewsParentAction.OpenBottomSheetFilter -> openBottomSheetEvent.invoke()
                    else -> {}
                }
            }
        }

        MainContent(
            viewModelNews = viewModelNews
        )
    }

    @Composable
    private fun MainContent(
        viewModelNews: NewsMainViewModel
    ) {
        Column(modifier = Modifier.fillMaxSize().background(color = MainBGColor)) {
            NewsList(viewModelNews)
        }
    }
}
