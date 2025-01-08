package tk.shkabaj.android.shkabaj.ui.settings.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.TextButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import nccloud.composeapp.generated.resources.Apply
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.ScreenSettingsTitle
import nccloud.composeapp.generated.resources.ic_check
import tk.shkabaj.android.shkabaj.extensions.getErrorMessage
import tk.shkabaj.android.shkabaj.extensions.injectScreenModel
import tk.shkabaj.android.shkabaj.modules.settings.SettingsStartTabAction
import tk.shkabaj.android.shkabaj.modules.settings.SettingsStartTabEvent
import tk.shkabaj.android.shkabaj.modules.settings.SettingsStartTabViewModel
import tk.shkabaj.android.shkabaj.navigation.tabs.TabItem
import tk.shkabaj.android.shkabaj.ui.customcomponents.showSnackBar
import tk.shkabaj.android.shkabaj.ui.theme.AccentColor
import tk.shkabaj.android.shkabaj.ui.theme.DarkGreyColor
import tk.shkabaj.android.shkabaj.ui.theme.DividerNewsColor
import tk.shkabaj.android.shkabaj.ui.theme.GreyColor
import tk.shkabaj.android.shkabaj.ui.theme.MaastrichtBlueColor
import tk.shkabaj.android.shkabaj.ui.theme.MainBGColor
import tk.shkabaj.android.shkabaj.ui.theme.Typography

class SettingsStartTabDialog(private val onDismiss: () -> Unit): Screen {

    @Composable
    override fun Content() {
        val viewModel = injectScreenModel<SettingsStartTabViewModel>()
        val state = viewModel.viewStates().collectAsState()
        val snackBarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            viewModel.viewActions().collect { uiAction ->
                when(uiAction) {
                    is SettingsStartTabAction.ShowError -> showSnackBar(
                        message = uiAction.error.getErrorMessage(),
                        snackBarHostState = snackBarHostState,
                        coroutine = coroutineScope
                    )
                    else -> {}
                }
            }
        }

        val screensList = state.value.tabs
        val selectedScreen = state.value.selectedTab

        Box(
            modifier = Modifier.background(color = MainBGColor)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp)
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                Header {
                    viewModel.obtainEvent(event = SettingsStartTabEvent.OnApplyClicked)
                    onDismiss.invoke()
                }
                screensList.forEach { tab ->
                    TabRow(
                        tabItem = tab,
                        selected = tab == selectedScreen,
                    ) {
                        viewModel.obtainEvent(SettingsStartTabEvent.OnStartTabSelected(tabItem = tab))
                    }
                    HorizontalDivider(
                        color = DividerNewsColor,
                        thickness = 0.5.dp
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
    private fun Header(onApplyClick: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 57.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.ScreenSettingsTitle),
                style = Typography.headlineMedium.copy(color = DarkGreyColor),
                modifier = Modifier.weight(1f)
            )
            TextButton(
                onClick = onApplyClick,
            ) {
                Text(
                    text = stringResource(Res.string.Apply),
                    style = Typography.labelMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = AccentColor
                    ),
                )
            }
        }
    }

    @Composable
    private fun TabRow(
        tabItem: TabItem,
        selected: Boolean,
        onScreenSelected: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 40.dp)
                .clickable { onScreenSelected() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = tabItem.title,
                style = Typography.bodyLarge.copy(color = MaastrichtBlueColor),
                modifier = Modifier.weight(1f)
            )
            AnimatedVisibility(
                visible = selected,
                enter = fadeIn() + expandIn(),
                exit = fadeOut() + shrinkOut()
            ) {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(resource = Res.drawable.ic_check),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = GreyColor)
                )
            }
        }

    }
}