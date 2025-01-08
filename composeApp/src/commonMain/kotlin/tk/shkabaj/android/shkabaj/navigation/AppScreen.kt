package tk.shkabaj.android.shkabaj.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import tk.shkabaj.android.shkabaj.ui.toolbar.ToolbarAction

object ToolbarActionHelper {
    private val _currentToolbarAction = Channel<ToolbarAction?>(Channel.BUFFERED)
    val currentToolbarAction: Flow<ToolbarAction?> = _currentToolbarAction.receiveAsFlow()

    fun sendToolbarAction(action: ToolbarAction) {
        CoroutineScope(Dispatchers.IO).launch {
            _currentToolbarAction.send(action)
        }
    }
}

sealed class AppScreenTitle {
    data class Res(val id: StringResource): AppScreenTitle()
    data class Str(val string: String): AppScreenTitle()

    @Composable
    fun getString(): String {
        return when(this) {
            is Res -> stringResource(id)
            is Str -> string
        }
    }
}

interface AppScreen: Screen {
    val title: AppScreenTitle?
    val toolbarActions: List<ToolbarAction>

    suspend fun handleToolbarAction(onToolbarActionEvent: (toolbarAction: ToolbarAction) -> Unit) {
        ToolbarActionHelper.currentToolbarAction.collect { toolbarAction ->
            if (toolbarAction == null) return@collect
            onToolbarActionEvent.invoke(toolbarAction)
        }
    }

}