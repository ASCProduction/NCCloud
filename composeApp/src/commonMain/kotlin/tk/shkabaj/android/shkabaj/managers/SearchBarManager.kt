package tk.shkabaj.android.shkabaj.managers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SearchBarManager {

    private val _isSearchBarFocused = MutableStateFlow(false)
    val isSearchBarFocused: StateFlow<Boolean> = _isSearchBarFocused

    fun setFocusedForSearchBar(isFocused: Boolean) {
        _isSearchBarFocused.value = isFocused
    }
}