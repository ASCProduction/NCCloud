package tk.shkabaj.android.shkabaj.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import tk.shkabaj.android.shkabaj.data.entity.NewsDatabaseEntity

@Dao
interface NewsDao {

    @Insert
    suspend fun insert(newsEntity: NewsDatabaseEntity)

    @Delete
    suspend fun delete(newsEntity: NewsDatabaseEntity)

    @Query("SELECT * FROM news WHERE articleUrl = :articleUrl")
    suspend fun getNewsByUrl(
        articleUrl: String,
    ): NewsDatabaseEntity?

    @Query("SELECT * FROM news ORDER BY bookmarkedDate DESC")
    fun getNews(): Flow<List<NewsDatabaseEntity>>
}