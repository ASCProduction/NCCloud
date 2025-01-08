package tk.shkabaj.android.shkabaj.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import tk.shkabaj.android.shkabaj.data.entity.UpdateDatabaseEntity

@Dao
interface UpdateDao {

    @Insert
    suspend fun insert(updateDatabaseEntity: UpdateDatabaseEntity)

    @Delete
    suspend fun delete(updateDatabaseEntity: UpdateDatabaseEntity)

    @Query("SELECT * FROM updateInfo")
    fun getUpdates(): Flow<List<UpdateDatabaseEntity>>

    @Query("UPDATE updateInfo SET updateInfoJson = :newUpdateInfoJson WHERE updateType = :updateType")
    suspend fun updateInfoJsonByType(updateType: String, newUpdateInfoJson: String)
}