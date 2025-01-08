package tk.shkabaj.android.shkabaj.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import tk.shkabaj.android.shkabaj.data.converters.TagsConverter
import tk.shkabaj.android.shkabaj.data.entity.NewsDatabaseEntity
import tk.shkabaj.android.shkabaj.data.entity.UpdateDatabaseEntity

@Database(
    entities = [
        NewsDatabaseEntity::class,
        UpdateDatabaseEntity::class],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
@TypeConverters(TagsConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getNewsDao(): NewsDao
    abstract fun getUpdatesDao(): UpdateDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}