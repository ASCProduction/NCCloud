package tk.shkabaj.android.shkabaj.modules.news

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import tk.shkabaj.android.shkabaj.base.BaseViewModel
import tk.shkabaj.android.shkabaj.extensions.launchSafe
import tk.shkabaj.android.shkabaj.managers.UpdatesManager
import tk.shkabaj.android.shkabaj.managers.bookmarks.NewsBookmarksManager
import tk.shkabaj.android.shkabaj.modules.main.model.NewsUpdateModel
import tk.shkabaj.android.shkabaj.modules.main.model.toJson
import tk.shkabaj.android.shkabaj.network.NCCloudApiService
import tk.shkabaj.android.shkabaj.network.entity.news.main.NewsEntity
import tk.shkabaj.android.shkabaj.ui.news.model.NewsFilterModel
import tk.shkabaj.android.shkabaj.ui.news.model.NewsItemModel
import tk.shkabaj.android.shkabaj.utils.Platform

data class MainNewsState(
    val newsList: List<NewsItemModel> = emptyList(),
    val currentFilterNewsType: NewsFilterModel = NewsFilterModel.DEFAULT,
    val progress: Boolean = false
)

sealed interface MainNewsAction {
    data class ShowError(val error: Throwable): MainNewsAction
}

sealed interface MainNewsEvent {
    data class OnBookmarkClick(val newsEntity: NewsEntity) : MainNewsEvent
    data object OnRefresh : MainNewsEvent
    data class OnShareClick(val newsEntity: NewsEntity): MainNewsEvent
    data class OnOpenUrl(val url: String): MainNewsEvent
    data class OnChangeFilterType(val newFilterType: NewsFilterModel): MainNewsEvent
}

class NewsMainViewModel(
    private val apiService: NCCloudApiService,
    private val newsBookmarksManager: NewsBookmarksManager,
    private val platform: Platform,
    private val updatesManager: UpdatesManager
) : BaseViewModel<MainNewsState, MainNewsAction, MainNewsEvent>(
    initialState = MainNewsState()
) {

    private var filterNewsType = NewsFilterModel.DEFAULT
    private var sourceNewsList = emptyList<NewsItemModel>()

    init {
        loadData()
        launchBookmarksCollecting()
    }

    private fun launchBookmarksCollecting() {
        screenModelScope.launch {
            newsBookmarksManager.getBookmarks().collectLatest { _ ->
                val newsList = viewState.newsList.map { newsItem ->
                    newsItem.copy(isBookmarked = newsBookmarksManager.isInBookmarks(newsItem.entity))
                }
                updateViewState { state ->
                    state.copy(newsList = newsList)
                }
            }
        }
    }

    private fun loadData() {
        screenModelScope.launchSafe(
            onAction = {
                updateViewState { state -> state.copy(progress = true) }

                val response = apiService.getNews()
                val news = response.map { item ->
                    NewsItemModel(
                        entity = item,
                        isBookmarked = newsBookmarksManager.isInBookmarks(item)
                    )
                }
                updateLastSeenNews(news = news)

                sourceNewsList = news
                val filteredNewsList = sortNewsList()
                updateViewState { state -> state.copy(newsList = filteredNewsList, progress = false) }
            },
            onError = ::handleError
        )
    }

    private suspend fun updateLastSeenNews(news: List<NewsItemModel>) {
        val updateNewsModel = news.map { NewsUpdateModel(
            articleUrl = it.entity.articleUrl ?: "",
            newsName = it.entity.title ?: ""
        ) }
        val updateNewsJson = updateNewsModel.toJson()
        updatesManager.updateInfoByKey(updateType = UpdatesManager.Key.NEWS, newUpdateInfo = updateNewsJson)
    }

    private fun updateNewsList(newFilterNewsType: NewsFilterModel) {
        filterNewsType = newFilterNewsType
        updateViewState { state ->
            val filteredNewsList = sortNewsList()
            state.copy(
                newsList = filteredNewsList,
                currentFilterNewsType = filterNewsType
            )
        }
    }

    private fun sortNewsList(): List<NewsItemModel> {
        return when(filterNewsType) {
            NewsFilterModel.DEFAULT -> sourceNewsList
            NewsFilterModel.DATE_FILTER -> sourceNewsList.sortedByDescending {
                it.entity.timeUpdated?.toInt()
            }
        }
    }

    private fun handleError(error: Throwable) {
        screenModelScope.launch {
            sendViewAction(action = MainNewsAction.ShowError(error = error))
            updateViewState { state ->
                state.copy(progress = false)
            }
        }
    }

    override fun obtainEvent(event: MainNewsEvent) {
        when (event) {
            is MainNewsEvent.OnBookmarkClick -> handleBookmarkClick(event.newsEntity)
            is MainNewsEvent.OnRefresh -> loadData()
            is MainNewsEvent.OnShareClick -> handleShareClick(event.newsEntity)
            is MainNewsEvent.OnOpenUrl -> platform.openUrl(url = event.url)
            is MainNewsEvent.OnChangeFilterType -> updateNewsList(newFilterNewsType = event.newFilterType)
        }
    }

    private fun handleBookmarkClick(newsEntity: NewsEntity) {
        screenModelScope.launchSafe(
            onAction = {
                if (newsBookmarksManager.isInBookmarks(newsEntity)) {
                    newsBookmarksManager.removeFromBookmarks(newsEntity)
                } else {
                    newsBookmarksManager.saveToBookmark(newsEntity)
                }
            },
            onError = ::handleError
        )
    }

    private fun handleShareClick(newsEntity: NewsEntity) {
        platform.shareNews(newsTitle = newsEntity.title ?: "")
    }
}