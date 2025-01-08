package tk.shkabaj.android.shkabaj.ui.customcomponents

import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun showSnackBar(
    message: String,
    snackBarHostState: SnackbarHostState,
    coroutine: CoroutineScope
) {
    coroutine.launch {
        snackBarHostState.showSnackbar(
            message = message,
            duration = SnackbarDuration.Short
        )
    }
}