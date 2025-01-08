package tk.shkabaj.android.shkabaj.base

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

abstract class BaseViewModel<State : Any, Action, Event>(
    initialState: State
) : ScreenModel, KoinComponent {

    private val _viewStates = MutableStateFlow(initialState)
    private val _viewActions = Channel<Action?>(Channel.BUFFERED)

    fun viewStates(): StateFlow<State> = _viewStates

    fun viewActions(): Flow<Action?> = _viewActions.receiveAsFlow()

    protected var viewState: State
        get() = _viewStates.value
        set(value) {
            _viewStates.value = value
        }

    private var viewAction: Action?
        get() = null
        set(value) {
            screenModelScope.launch {
                _viewActions.send(value)
            }
        }

    fun updateViewState(function: (State) -> State) {
        _viewStates.update(function::invoke)
    }

    protected fun sendViewAction(action: Action?) {
        viewAction = action
    }

    open fun obtainEvent(event: Event) = Unit
}
