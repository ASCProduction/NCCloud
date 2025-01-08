package tk.shkabaj.android.shkabaj.player

import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryOptionAllowBluetoothA2DP
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.AVAudioSessionModeDefault
import platform.AVFAudio.setActive
import platform.UIKit.UIApplication
import platform.UIKit.beginReceivingRemoteControlEvents

object AudioSessionHelper {

    private val session = AVAudioSession.sharedInstance()

    @OptIn(ExperimentalForeignApi::class)
    fun configureSession() {
        val options = AVAudioSessionCategoryOptionAllowBluetoothA2DP
        session.setCategory(
            AVAudioSessionCategoryPlayback,
            AVAudioSessionModeDefault,
            options,
            null
        )
        session.setActive(true, null)

        //needed for MPNowPlayingInfoCenter work
        UIApplication.sharedApplication().beginReceivingRemoteControlEvents()
    }

}