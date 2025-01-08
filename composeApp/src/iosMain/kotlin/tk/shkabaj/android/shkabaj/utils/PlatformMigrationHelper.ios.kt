package tk.shkabaj.android.shkabaj.utils

import com.russhwolf.settings.contains
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import platform.Foundation.NSArray
import platform.Foundation.NSData
import platform.Foundation.NSDate
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSKeyedUnarchiver
import platform.Foundation.NSNumber
import platform.Foundation.NSPropertyListSerialization
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.arrayWithContentsOfFile
import platform.Foundation.dataWithContentsOfFile
import platform.Foundation.timeIntervalSince1970
import platform.darwin.NSInteger
import tk.shkabaj.android.shkabaj.data.entity.LocalizedDbEntity
import tk.shkabaj.android.shkabaj.data.entity.NewsDatabaseEntity
import tk.shkabaj.android.shkabaj.data.entity.RadioDatabaseEntity
import tk.shkabaj.android.shkabaj.data.entity.StatisticDbEntity
import tk.shkabaj.android.shkabaj.data.entity.ThumbnailsDbEntity
import tk.shkabaj.android.shkabaj.data.entity.ThumbnailsItemDbEntity
import tk.shkabaj.android.shkabaj.data.entity.VideoContentDetailsDbEntity
import tk.shkabaj.android.shkabaj.data.entity.VideoDatabaseEntity
import tk.shkabaj.android.shkabaj.data.entity.VideoSnippetDbEntity
import tk.shkabaj.android.shkabaj.data.local.AppPreferencesFactory
import tk.shkabaj.android.shkabaj.data.local.PreferencesKey
import tk.shkabaj.android.shkabaj.data.local.PreferencesManager
import tk.shkabaj.android.shkabaj.extensions.formatListeningCount
import tk.shkabaj.android.shkabaj.managers.CountriesManager
import tk.shkabaj.android.shkabaj.managers.SettingsManager
import tk.shkabaj.android.shkabaj.managers.bookmarks.NewsBookmarksManager
import tk.shkabaj.android.shkabaj.managers.bookmarks.RadioBookmarksManager
import tk.shkabaj.android.shkabaj.managers.bookmarks.VideoBookmarksManager
import tk.shkabaj.android.shkabaj.navigation.tabs.TabItem

actual class PlatformMigrationHelper: KoinComponent {

    private val settings = AppPreferencesFactory.createDefaultPreferences()
    private val settingsManager by inject<SettingsManager>()
    private val countriesManager by inject<CountriesManager>()
    private val preferences by inject<PreferencesManager>()
    private val newsBookmarksManager by inject<NewsBookmarksManager>()
    private val radioBookmarksManager by inject<RadioBookmarksManager>()
    private val videoBookmarksManager by inject<VideoBookmarksManager>()

    private val fileManager = NSFileManager.defaultManager()
    private val docsPath = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, true).first()
    private val docsURL = NSURL.URLWithString(docsPath as String)

    actual suspend fun migratePreferences(): Boolean {
        val isGDPRApplicableKey = "isGDPRApplicable"
        val kSHConsentV2StringKey = "kSHConsentV2String"
        val kSHGDPRPresentCountKey = "kSHGDPRPresentCount"
        val startScreenKey = "startScreenKey"
        val countryIndexKey = "countryIndex"

        val keys = listOf(
            isGDPRApplicableKey,
            kSHConsentV2StringKey,
            kSHGDPRPresentCountKey,
            startScreenKey,
            countryIndexKey
        ).filter { settings.contains(it) }.toMutableList()

        if (keys.isEmpty()) {
            println("MigrationHelper migration keys not exist")
            return true
        }

        val removeKey: (String) -> Unit = {
            keys.remove(it)
            settings.remove(it)
        }

        val isGDPRApplicable = settings.getBooleanOrNull(isGDPRApplicableKey)
        isGDPRApplicable?.let {
            preferences.putBoolean(PreferencesKey.IS_GDPR_APPLICABLE, it)
            removeKey(isGDPRApplicableKey)
        }

        val kSHConsentV2String = settings.getStringOrNull(kSHConsentV2StringKey)
        kSHConsentV2String?.let {
            preferences.putString(PreferencesKey.CONSENT_STRING, it)
            removeKey(kSHConsentV2StringKey)
        }

        val kSHGDPRPresentCount = settings.getIntOrNull(kSHGDPRPresentCountKey)
        kSHGDPRPresentCount?.let {
            preferences.putInt(PreferencesKey.GDPR_PRESENT_COUNT, it)
            removeKey(kSHGDPRPresentCountKey)
        }

        val startScreen = settings.getStringOrNull(startScreenKey)
        startScreen?.let {
            when(startScreen) {
                "Ballina" -> { settingsManager.setStartTab(TabItem.HOME) }
                "Lajme" -> { settingsManager.setStartTab(TabItem.NEWS) }
                "Radio" -> { settingsManager.setStartTab(TabItem.RADIO) }
                "Video" -> { settingsManager.setStartTab(TabItem.VIDEO) }
                "Moti" -> { settingsManager.setStartTab(TabItem.WEATHER) }
                else -> {}
            }
            removeKey(startScreenKey)
        }

        val countryIndex = settings.getIntOrNull(countryIndexKey)
        countryIndex?.let { index ->
            runCatching {
                countriesManager.loadCountries().getOrNull(index)?.let {
                    countriesManager.setSelectedCountryName(it.name)
                    removeKey(countryIndexKey)
                }
            }
        }

        return keys.isEmpty()
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun migrateNewsBookmarks(): Boolean {
        val newsFile = docsURL?.URLByAppendingPathComponent("Bookmarks")?.path ?: return false
        if (fileManager.fileExistsAtPath(newsFile).not()) {
            println("MigrationHelper news file not exist")
            return true
        }

        val bookmarks = NSArray.arrayWithContentsOfFile(newsFile) as? List<Map<String, Any>>
        val news = bookmarks?.let { map ->
            map.mapNotNull {
                val articleURL = it["articleURI"] as? String ?: return@mapNotNull null
                val title = it["title"] as? String ?: return@mapNotNull null
                val bookmarkedDate = it["favoritedDate"] as? NSDate
                NewsDatabaseEntity(
                    articleUrl = articleURL,
                    title = title,
                    description = "",
                    bookmarkedDate = bookmarkedDate?.timeIntervalSince1970
                )
            }
        } ?: emptyList()

        newsBookmarksManager.appendBookmarks(news)
        fileManager.removeItemAtPath(newsFile, null)
        println("MigrationHelper migrated ${news.count()} news")
        return true
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun migrateRadioBookmarks(): Boolean {
        val radiosFile = docsURL?.URLByAppendingPathComponent("RadioFavoritesIds")?.path ?: return false
        if (fileManager.fileExistsAtPath(radiosFile).not()) {
            println("MigrationHelper radios file not exist")
            return true
        }

        val serializedRadios = NSKeyedUnarchiver.unarchiveObjectWithFile(radiosFile) as? List<Map<String, Any>>
        val radiosEntity = serializedRadios?.let { map ->
            map.mapNotNull {
                val radioId = it["radio_id"] as? NSNumber ?: return@mapNotNull null
                val name = it["name"] as? String ?: return@mapNotNull null
                val site = it["site"] as? String ?: return@mapNotNull null
                val city = it["city"] as? String ?: return@mapNotNull null
                val url = (it["url"] as? NSURL)?.absoluteString ?: return@mapNotNull null
                val imageName = it["image_name"] as? String ?: return@mapNotNull null
                val imagePath = it["image_path"] as? String ?: return@mapNotNull null
                val listeningCount = it["listening_count"] as? NSNumber ?: return@mapNotNull null

                RadioDatabaseEntity(
                    radioId = radioId.intValue,
                    radioName = name,
                    radioSite = site,
                    radioCity = city,
                    radioURLString = url,
                    radioImage = "$imagePath$imageName",
                    sorting = 0,
                    listeningCountInt = listeningCount.intValue,
                    listeningCount = listeningCount.intValue.formatListeningCount(),
                    thumbsUpCount = 0,
                    bookmarkedDate = DateTimeUtils.currentTimestamp
                )
            }
        } ?: emptyList()

        radioBookmarksManager.appendRadios(radiosEntity)
        fileManager.removeItemAtPath(radiosFile, null)
        println("MigrationHelper migrated ${radiosEntity.count()} radios")
        return true
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun migrateVideoBookmarks(): Boolean {
        val videosFile = docsURL?.URLByAppendingPathComponent("video_bookmarks")?.path ?: return false
        if (fileManager.fileExistsAtPath(videosFile).not()) {
            println("MigrationHelper videos file not exist")
            return true
        }

        val data = NSData.dataWithContentsOfFile(videosFile)
        val array = data?.let {
            NSPropertyListSerialization.propertyListWithData(
                data = it,
                options = 0u,
                format = null,
                error = null
            )
        } as? List<Map<String, Any>>

        val videos = array?.let { map ->
            map.mapNotNull {
                val id = it["videoID"] as? String ?: return@mapNotNull null
                val publishDate = it["publishDate"] as? NSDate ?: return@mapNotNull null
                val title = it["title"] as? String ?: return@mapNotNull null
                val description = it["description1"] as? String ?: return@mapNotNull null
                val thumbnailUrl = it["thumbnailUrl"] as? String ?: return@mapNotNull null
                val duration = it["duration"] as? NSInteger ?: 0
                val viewCount = it["viewCount"] as? NSInteger ?: 0

                VideoDatabaseEntity(
                    kind = "",
                    id = id,
                    snippet = VideoSnippetDbEntity(
                        publishedAt = DateTimeUtils.dateStringFromTimestamp(publishDate.timeIntervalSince1970) ?: "",
                        channelId = "",
                        title = title,
                        description = description,
                        thumbnails = ThumbnailsDbEntity(
                            default = ThumbnailsItemDbEntity(
                                url = thumbnailUrl,
                                width = 200,
                                height = 200
                            ),
                            medium = ThumbnailsItemDbEntity(
                                url = thumbnailUrl,
                                width = 200,
                                height = 200
                            ),
                            high = ThumbnailsItemDbEntity(
                                url = thumbnailUrl,
                                width = 200,
                                height = 200
                            )
                        ),
                        channelTitle = "",
                        categoryId = "",
                        tags = emptyList(),
                        liveBroadcastContent = "",
                        localized = LocalizedDbEntity(
                            title = title,
                            description = description
                        )
                    ),
                    contentDetails = VideoContentDetailsDbEntity(
                        duration = formatSecondsToString(duration),
                        dimension = "",
                        definition = "",
                        caption = "",
                        licensedContent = true
                    ),
                    statistics = StatisticDbEntity(
                        viewCount = viewCount.toString(),
                        likeCount = "",
                        favoriteCount = "",
                        commentCount = ""
                    ),
                    bookmarkedDate = DateTimeUtils.currentTimestamp
                )

            }
        } ?: emptyList()

        videoBookmarksManager.appendVideos(videos)
        fileManager.removeItemAtPath(videosFile, null)
        println("MigrationHelper migrated ${videos.count()} videos")
        return true
    }

    private fun formatSecondsToString(seconds: NSInteger): String {
        val h = seconds / 3600
        val m = (seconds / 60) % 60
        val s = seconds % 60
        val hString = h.toString().padStart(2, '0')
        val mString = m.toString().padStart(2, '0')
        val sString = s.toString().padStart(2, '0')
        return if (h > 0) "$hString:$mString:$sString" else "$mString:$sString"
    }

}