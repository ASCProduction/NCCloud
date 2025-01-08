package tk.shkabaj.android.shkabaj.data.local.db

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import tk.shkabaj.android.shkabaj.data.db.AppDatabase

private fun getAppDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = dbDirectory() + "/app.db"
    return Room.databaseBuilder<AppDatabase>(name = dbFilePath).apply {
        setDriver(BundledSQLiteDriver())
        setQueryCoroutineContext(Dispatchers.IO)
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun dbDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )
    return requireNotNull(documentDirectory?.path)
}

fun getAppDatabase() = getAppDatabaseBuilder().build()