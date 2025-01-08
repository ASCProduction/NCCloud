package tk.shkabaj.android.shkabaj.data.local.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import tk.shkabaj.android.shkabaj.data.db.AppDatabase

private fun getAppDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = context.applicationContext
    val file = appContext.getDatabasePath("app.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = file.absolutePath
    ).apply {
        setDriver(BundledSQLiteDriver())
        setQueryCoroutineContext(Dispatchers.IO)
    }
}

fun getAppDatabase(context: Context) = getAppDatabaseBuilder(context).build()