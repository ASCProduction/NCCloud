package tk.shkabaj.android.shkabaj.managers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import tk.shkabaj.android.shkabaj.data.db.AppDatabase
import tk.shkabaj.android.shkabaj.data.entity.UpdateDatabaseEntity
import tk.shkabaj.android.shkabaj.data.entity.toNewsUpdateModelList
import tk.shkabaj.android.shkabaj.navigation.tabs.TabItem
import tk.shkabaj.android.shkabaj.network.NCCloudApiService
import tk.shkabaj.android.shkabaj.ui.news.model.NewsItemModel

class UpdatesManager(
    private val appDatabase: AppDatabase,
    private val apiService: NCCloudApiService
) {

    enum class Key {
        NEWS;

        val tabItem: TabItem
            get() = when(this) {
                NEWS -> TabItem.NEWS
            }
    }

    private val updateDao by lazy { appDatabase.getUpdatesDao() }
    private val scope = CoroutineScope(Dispatchers.IO)

    suspend fun updateInfoByKey(updateType: Key, newUpdateInfo: String) {
        updateDao.updateInfoJsonByType(updateType = updateType.name.lowercase(), newUpdateInfoJson = newUpdateInfo)
    }

    suspend fun checkUpdates(): List<TabItem> {
        val updatesList = updateDao.getUpdates().first()
        if (updatesList.isEmpty()) {
            Key.entries.forEach {
                updateDao.insert(updateDatabaseEntity = UpdateDatabaseEntity(
                    updateType = it.name.lowercase(),
                    updateInfoJson = "[]"
                ))
            }
            return Key.entries.map { it.tabItem }
        }

        val newsList = scope.async { getNewsList() }
        val finalNewsList = newsList.await()

        val newsOldList = updatesList.find {
            it.updateType == Key.NEWS.name.lowercase()
        }?.updateInfoJson?.toNewsUpdateModelList()

        val isNewsHaveUpdates = checkUpdate(newsOldList, finalNewsList) { oldItem, newItem ->
            oldItem.articleUrl != newItem.entity.articleUrl || oldItem.newsName != newItem.entity.title
        }

        val updateKeys = mutableListOf<Key>()
        if (isNewsHaveUpdates) updateKeys.add(Key.NEWS)
        return updateKeys.map { it.tabItem }
    }

    private suspend fun getNewsList(): List<NewsItemModel> {
        return apiService.getNews().map {
            NewsItemModel(entity = it, isBookmarked = false)
        }
    }

    private fun <Old, New> checkUpdate(
        oldList: List<Old>?,
        newList: List<New>,
        compare: (Old, New) -> Boolean
    ): Boolean {
        if (oldList == null) return true
        if (oldList.size != newList.size) return true
        return oldList.indices.any { index ->
            val oldItem = oldList[index]
            val newItem = newList[index]
            compare(oldItem, newItem)
        }
    }

}