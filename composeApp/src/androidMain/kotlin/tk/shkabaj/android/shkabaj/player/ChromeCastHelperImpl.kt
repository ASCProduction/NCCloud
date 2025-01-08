package tk.shkabaj.android.shkabaj.player

import android.net.Uri
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaLoadRequestData
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManagerListener
import com.google.android.gms.common.images.WebImage
import tk.shkabaj.android.shkabaj.NCCloudApp

class ChromeCastHelperImpl: ChromeCastHelper {

    private val context = NCCloudApp.context

    override var eventsHandler: ((ChromeCastHelper.Event) -> Unit)? = null

    private val castContext: CastContext = CastContext.getSharedInstance(context)

    init {
        CastContext.getSharedInstance(context).sessionManager.addSessionManagerListener(
            object : SessionManagerListener<CastSession> {
                override fun onSessionStarted(session: CastSession, sessionId: String) {
                    eventsHandler?.invoke(ChromeCastHelper.Event.StartedCastSession)
                }

                override fun onSessionEnded(session: CastSession, error: Int) {
                    eventsHandler?.invoke(ChromeCastHelper.Event.EndedCastSession)
                }

                override fun onSessionEnding(p0: CastSession) {
                    eventsHandler?.invoke(ChromeCastHelper.Event.EndedCastSession)
                }

                override fun onSessionResumeFailed(p0: CastSession, p1: Int) {
                    eventsHandler?.invoke(ChromeCastHelper.Event.EndedCastSession)
                }

                override fun onSessionResumed(p0: CastSession, p1: Boolean) {
                    eventsHandler?.invoke(ChromeCastHelper.Event.StartedCastSession)
                }

                override fun onSessionResuming(p0: CastSession, p1: String) {
                    TODO("Not yet implemented")
                }

                override fun onSessionStartFailed(p0: CastSession, p1: Int) {
                    eventsHandler?.invoke(ChromeCastHelper.Event.EndedCastSession)
                }

                override fun onSessionStarting(p0: CastSession) {
                    eventsHandler?.invoke(ChromeCastHelper.Event.StartedCastSession)
                }

                override fun onSessionSuspended(p0: CastSession, p1: Int) {
                    eventsHandler?.invoke(ChromeCastHelper.Event.EndedCastSession)
                }

            }, CastSession::class.java
        )
    }

    override fun isCastConnected(): Boolean {
        val session = castContext.sessionManager.currentCastSession
        return session != null && session.isConnected
    }

    override fun endCastSession() {
        castContext.sessionManager.endCurrentSession(true)
    }

    override fun castItem(item: PlayerItem, startImmediately: Boolean) {
        val mediaMetadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MUSIC_TRACK).apply {
            putString(MediaMetadata.KEY_TITLE, item.title ?: "")
            putString(MediaMetadata.KEY_SUBTITLE, item.subtitle ?: "")
            putString(MediaMetadata.KEY_ARTIST, item.subtitle ?: "")
            item.imageUrl?.let {
                val webImage = WebImage(Uri.parse(it))
                addImage(webImage)
            }
        }

        val mediaInfo = MediaInfo.Builder(item.mediaUrl)
            .setMetadata(mediaMetadata)
            .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
            .build()

        val mediaLoadRequestData = MediaLoadRequestData.Builder()
            .setMediaInfo(mediaInfo)
            .setAutoplay(startImmediately)
            .build()

        val castSession = castContext.sessionManager.currentCastSession
        castSession?.remoteMediaClient?.load(mediaLoadRequestData)
    }

    override fun play() {
        val castSession = castContext.sessionManager.currentCastSession
        castSession?.remoteMediaClient?.play()
    }

    override fun pause() {
        val castSession = castContext.sessionManager.currentCastSession
        castSession?.remoteMediaClient?.pause()
    }

    override fun stop() {
        val castSession = castContext.sessionManager.currentCastSession
        castSession?.remoteMediaClient?.stop()
    }
}