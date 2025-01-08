package tk.shkabaj.android.shkabaj.managers.bookmarks

import kotlinx.coroutines.flow.Flow
import tk.shkabaj.android.shkabaj.data.db.AppDatabase
import tk.shkabaj.android.shkabaj.data.entity.NewsDatabaseEntity
import tk.shkabaj.android.shkabaj.network.entity.news.main.NewsEntity
import tk.shkabaj.android.shkabaj.network.entity.news.main.toDatabaseEntity
import tk.shkabaj.android.shkabaj.utils.DateTimeUtils

class NewsBookmarksManager(private val appDatabase: AppDatabase) {

    private val newsDao by lazy { appDatabase.getNewsDao() }

    fun getBookmarks(): Flow<List<NewsDatabaseEntity>> {
        return newsDao.getNews()
    }

    suspend fun appendBookmarks(bookmarks: List<NewsDatabaseEntity>) {
        bookmarks.forEach { newsDao.insert(it) }
    }

    suspend fun saveToBookmark(newsEntity: NewsEntity) {
        newsDao.insert(
            newsEntity.toDatabaseEntity(
                bookmarkedDate = DateTimeUtils.currentTimestamp
            )
        )
    }

    suspend fun removeFromBookmarks(newsEntity: NewsEntity) {
        newsDao.delete(newsEntity.toDatabaseEntity())
    }

    suspend fun isInBookmarks(newsEntity: NewsEntity): Boolean {
        return newsDao.getNewsByUrl(articleUrl = newsEntity.articleUrl ?: "") != null
    }

}