package tk.shkabaj.android.shkabaj.player

import platform.MediaPlayer.MPRemoteCommandCenter
import platform.MediaPlayer.MPRemoteCommandHandlerStatusSuccess

object MPRemoteControlsManager {

    enum class Action {
        PLAY, PAUSE, NEXT,PREV
    }

    private val remoteCommandCenter = MPRemoteCommandCenter.sharedCommandCenter()

    fun addRemoteControlsHandlers(actionHandler: (Action) -> (Unit)): List<Any> {
        val targets: MutableList<Any> = mutableListOf()
        targets.add(remoteCommandCenter.playCommand.addTargetWithHandler {
            actionHandler.invoke(Action.PLAY)
            return@addTargetWithHandler MPRemoteCommandHandlerStatusSuccess
        })

        targets.add(remoteCommandCenter.pauseCommand.addTargetWithHandler {
            actionHandler.invoke(Action.PAUSE)
            return@addTargetWithHandler MPRemoteCommandHandlerStatusSuccess
        })

        targets.add(remoteCommandCenter.nextTrackCommand.addTargetWithHandler {
            actionHandler.invoke(Action.NEXT)
            return@addTargetWithHandler MPRemoteCommandHandlerStatusSuccess
        })

        targets.add(remoteCommandCenter.previousTrackCommand.addTargetWithHandler {
            actionHandler.invoke(Action.PREV)
            return@addTargetWithHandler MPRemoteCommandHandlerStatusSuccess
        })

        return targets
    }

    fun removeRemoteControlsHandlers(targets: List<Any>) {
        val commands = listOf(remoteCommandCenter.playCommand,  remoteCommandCenter.pauseCommand,
            remoteCommandCenter.nextTrackCommand, remoteCommandCenter.previousTrackCommand)
        commands.zip(targets).forEach {
            it.first.removeTarget(it.second)
        }
    }

    fun setEnableStateForRemotePlayerButtons(enabled: Boolean) {
        remoteCommandCenter.nextTrackCommand.setEnabled(enabled)
        remoteCommandCenter.previousTrackCommand.setEnabled(enabled)
        remoteCommandCenter.playCommand.setEnabled(enabled)
        remoteCommandCenter.pauseCommand.setEnabled(enabled)
    }

}