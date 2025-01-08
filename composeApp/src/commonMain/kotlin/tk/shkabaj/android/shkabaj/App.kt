package tk.shkabaj.android.shkabaj

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import org.jetbrains.compose.ui.tooling.preview.Preview
import tk.shkabaj.android.shkabaj.navigation.MainScreen
import tk.shkabaj.android.shkabaj.ui.theme.AccentColor
import tk.shkabaj.android.shkabaj.ui.theme.MainBGColor

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme (
        colorScheme = MaterialTheme.colorScheme.copy(
            background = Color.White,
            surfaceContainerHigh = Color.White,
            onSurfaceVariant = AccentColor
        )
    ) {
        Box(Modifier.background(MainBGColor)) {
            BottomSheetNavigator(
                sheetBackgroundColor = Color.White,
                sheetShape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp)
            ) {
                Navigator(screen = MainScreen())
            }
        }
    }
}