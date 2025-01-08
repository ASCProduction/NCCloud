package tk.shkabaj.android.shkabaj.modules.news

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import tk.shkabaj.android.shkabaj.base.BaseViewModel
import tk.shkabaj.android.shkabaj.data.entity.toDomainEntity
import tk.shkabaj.android.shkabaj.extensions.launchSafe
import tk.shkabaj.android.shkabaj.managers.bookmarks.NewsBookmarksManager
import tk.shkabaj.android.shkabaj.network.entity.news.main.NewsEntity
import tk.shkabaj.android.shkabaj.ui.news.model.NewsItemModel
import tk.shkabaj.android.shkabaj.ui.toolbar.ToolbarAction
import tk.shkabaj.android.shkabaj.utils.analytics.AnalyticsTracker
import tk.shkabaj.android.shkabaj.utils.Platform

data class NewsBookmarksState(
    val bookmarks: List<NewsItemModel> = emptyList()
)

sealed interface NewsBookmarksAction {
    data class ShowError(val error: Throwable): NewsBookmarksAction
    data object BackNavigation: NewsBookmarksAction
}

sealed interface NewsBookmarksEvent {
    data class OnBookmarkClick(val newsEntity: NewsEntity) : NewsBookmarksEvent
    data class OnOpenUrl(val newsItem: NewsEntity): NewsBookmarksEvent
    data class OnToolbarEvent(val toolbarAction: ToolbarAction): NewsBookmarksEvent
}

class NewsBookmarksViewModel(
    private val newsBookmarksManager: NewsBookmarksManager,
    private val platform: Platform
) : BaseViewModel<NewsBookmarksState, NewsBookmarksAction, NewsBookmarksEvent>(
    initialState = NewsBookmarksState()
) {

    init {
        screenModelScope.launchSafe(
            onAction = {
                newsBookmarksManager.getBookmarks().collectLatest { bookmarks ->
                    updateViewState { state ->
                        state.copy(
                            bookmarks = bookmarks.map { bookmark ->
                                NewsItemModel(
                                    entity = bookmark.toDomainEntity(),
                                    isBookmarked = true,
                                    bookmarkedDate = bookmark.bookmarkedDate
                                )
                            }
                        )
                    }
                }
            },
            onError = ::handleError
        )
    }

    private fun handleError(error: Throwable) {
        screenModelScope.launch {
            sendViewAction(action = NewsBookmarksAction.ShowError(error = error))
        }
    }

    private fun handleToolbarAction(toolbarAction: ToolbarAction) {
        when(toolbarAction) {
            ToolbarAction.BACK_NAVIGATION -> sendViewAction(action = NewsBookmarksAction.BackNavigation)
            else -> {}
        }
    }

    override fun obtainEvent(event: NewsBookmarksEvent) {
        when (event) {
            is NewsBookmarksEvent.OnBookmarkClick -> deleteBookmark(event.newsEntity)
            is NewsBookmarksEvent.OnOpenUrl -> {
                AnalyticsTracker.trackNewsBookmarksEvent(title = event.newsItem.title ?: "")
                platform.openUrl(url = event.newsItem.articleUrl ?: "")
            }
            is NewsBookmarksEvent.OnToolbarEvent -> handleToolbarAction(toolbarAction = event.toolbarAction)
        }
    }

    private fun deleteBookmark(newsEntity: NewsEntity) {
        screenModelScope.launchSafe(
            onAction = { newsBookmarksManager.removeFromBookmarks(newsEntity) },
            onError = ::handleError
        )
    }
}