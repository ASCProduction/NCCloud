package tk.shkabaj.android.shkabaj.ui.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import tk.shkabaj.android.shkabaj.modules.news.NewsParentEvent
import tk.shkabaj.android.shkabaj.modules.news.NewsParentViewModel
import tk.shkabaj.android.shkabaj.extensions.injectScreenModel
import tk.shkabaj.android.shkabaj.modules.news.NewsMainViewModel
import tk.shkabaj.android.shkabaj.modules.news.NewsParentAction
import tk.shkabaj.android.shkabaj.ui.news.components.NewsList
import tk.shkabaj.android.shkabaj.ui.theme.MainBGColor
import tk.shkabaj.android.shkabaj.navigation.AppScreen
import tk.shkabaj.android.shkabaj.navigation.AppScreenTitle
import tk.shkabaj.android.shkabaj.ui.toolbar.ToolbarAction

class NewsScreen : AppScreen {

    override val title: AppScreenTitle? = null
    override val toolbarActions: List<ToolbarAction>
        get() = listOf(ToolbarAction.PLAYER, ToolbarAction.FAVOURITE, ToolbarAction.FILTER)

    @Composable
    override fun Content() {
        val viewModel = injectScreenModel<NewsParentViewModel>()
        val viewModelNews = injectScreenModel<NewsMainViewModel>()

        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            handleToolbarAction { toolbarAction ->
                viewModel.obtainEvent(event = NewsParentEvent.OnToolbarEvent(
                    toolbarAction = toolbarAction
                ))
            }
        }

        val openBottomSheetEvent: () -> Unit = {

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
