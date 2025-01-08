package tk.shkabaj.android.shkabaj.ui.settings.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.SelectTheme
import nccloud.composeapp.generated.resources.ic_circle_filled_16
import nccloud.composeapp.generated.resources.ic_circle_outlined_16
import nccloud.composeapp.generated.resources.ic_close
import tk.shkabaj.android.shkabaj.extensions.currentBottomSheetNavigator
import tk.shkabaj.android.shkabaj.extensions.getErrorMessage
import tk.shkabaj.android.shkabaj.extensions.injectScreenModel
import tk.shkabaj.android.shkabaj.modules.settings.SettingsThemeAction
import tk.shkabaj.android.shkabaj.modules.settings.SettingsThemeEvent
import tk.shkabaj.android.shkabaj.modules.settings.SettingsThemeViewModel
import tk.shkabaj.android.shkabaj.ui.customcomponents.showSnackBar
import tk.shkabaj.android.shkabaj.ui.settings.AppTheme
import tk.shkabaj.android.shkabaj.ui.theme.DarkGreyColor
import tk.shkabaj.android.shkabaj.ui.theme.MaastrichtBlueColor
import tk.shkabaj.android.shkabaj.ui.theme.MainBGColor
import tk.shkabaj.android.shkabaj.ui.theme.RadioPlayerActionColor
import tk.shkabaj.android.shkabaj.ui.theme.Typography
import tk.shkabaj.android.shkabaj.ui.theme.WhiteColor

class SettingsThemeDialog(private val onChanged: () -> Unit) : Screen {

    @Composable
    override fun Content() {
        val viewModel = injectScreenModel<SettingsThemeViewModel>()
        val state = viewModel.viewStates().collectAsState()
        val snackBarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        val selectedTheme = state.value.selectedTheme

        val onCardClickable: (newTheme: AppTheme, isDark: Boolean) -> Unit = { newTheme, isDark ->
            viewModel.obtainEvent(event = SettingsThemeEvent.OnSelectedNewTheme(
                newTheme = newTheme,
                isDark = isDark
            ))
        }

        LaunchedEffect(Unit) {
            viewModel.viewActions().collect { uiAction ->
                when(uiAction) {
                    is SettingsThemeAction.ShowError -> showSnackBar(
                        message = uiAction.error.getErrorMessage(),
                        snackBarHostState = snackBarHostState,
                        coroutine = coroutineScope
                    )
                    else -> { }
                }
            }
        }

        Box(
            modifier = Modifier.background(color = MainBGColor)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp)
                    .windowInsetsPadding(WindowInsets.navigationBars),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Header()
                AppTheme.entries.forEach {
                    ThemeCard(
                        theme = it,
                        selected = selectedTheme == it,
                        onCardClickable = onCardClickable
                    )
                }
            }
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier
                    .align(alignment = Alignment.BottomCenter)
                    .padding(all = 16.dp)
            )
        }
    }

    @Composable
    private fun Header() {
        val bottomSheetNavigator = currentBottomSheetNavigator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 57.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.SelectTheme),
                style = Typography.headlineMedium.copy(color = DarkGreyColor),
                modifier = Modifier.weight(1f)
            )
            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = { bottomSheetNavigator.hide() },
            ) {
                Icon(
                    painter = painterResource(resource = Res.drawable.ic_close),
                    contentDescription = null,
                    tint = RadioPlayerActionColor
                )
            }
        }
    }

    @Composable
    private fun ThemeCard(
        theme: AppTheme,
        selected: Boolean,
        onCardClickable: (selectedTheme: AppTheme, isDark: Boolean) -> Unit
    ) {
        val isDark = theme.isDark
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(88.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = WhiteColor)
        ) {
            Row(
                modifier = Modifier
                    .clickable {
                        onCardClickable(theme, isDark)
                        onChanged.invoke()
                    }
                    .padding(12.dp)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Image(
                        painter = painterResource(resource = theme.icon),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = theme.title,
                        style = Typography.bodyLarge.copy(color = MaastrichtBlueColor),
                    )
                }
                Crossfade(if (selected) {
                    Res.drawable.ic_circle_filled_16
                } else {
                    Res.drawable.ic_circle_outlined_16
                }) { image ->
                    Image(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(image),
                        contentDescription = null
                    )
                }
            }
        }
    }

}