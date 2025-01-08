package tk.shkabaj.android.shkabaj.extensions

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator

val currentNavigator: Navigator?
    @Composable
    get() = LocalNavigator.current

val currentParentNavigator: Navigator?
    @Composable
    get() = LocalNavigator.current?.parent

val currentBottomSheetNavigator: BottomSheetNavigator
    @Composable
    get() = LocalBottomSheetNavigator.current