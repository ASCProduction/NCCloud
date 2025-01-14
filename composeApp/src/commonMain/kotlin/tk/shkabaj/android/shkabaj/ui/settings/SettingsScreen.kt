package tk.shkabaj.android.shkabaj.ui.settings

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import nccloud.composeapp.generated.resources.AllRightsReserved
import nccloud.composeapp.generated.resources.AppVersion
import nccloud.composeapp.generated.resources.Application
import nccloud.composeapp.generated.resources.CacheRemovedMsg
import nccloud.composeapp.generated.resources.DeleteCache
import nccloud.composeapp.generated.resources.HomePage
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.SelectTheme
import nccloud.composeapp.generated.resources.SettingsTitle
import nccloud.composeapp.generated.resources.ic_chevron_right_24
import nccloud.composeapp.generated.resources.ic_recycling
import tk.shkabaj.android.shkabaj.extensions.currentBottomSheetNavigator
import tk.shkabaj.android.shkabaj.extensions.getErrorMessage
import tk.shkabaj.android.shkabaj.extensions.injectScreenModel
import tk.shkabaj.android.shkabaj.modules.settings.SettingsEvent
import tk.shkabaj.android.shkabaj.modules.settings.SettingsViewModel
import tk.shkabaj.android.shkabaj.ui.customcomponents.showSnackBar
import tk.shkabaj.android.shkabaj.ui.settings.components.SettingsStartTabDialog
import tk.shkabaj.android.shkabaj.ui.settings.components.SettingsThemeDialog
import tk.shkabaj.android.shkabaj.ui.theme.Afacad
import tk.shkabaj.android.shkabaj.ui.theme.BlueColor
import tk.shkabaj.android.shkabaj.ui.theme.DividerNewsColor
import tk.shkabaj.android.shkabaj.ui.theme.MaastrichtBlueColor
import tk.shkabaj.android.shkabaj.ui.theme.MainBGColor
import tk.shkabaj.android.shkabaj.ui.theme.OldSilverColor
import tk.shkabaj.android.shkabaj.ui.theme.RadioPlayerActionColor
import tk.shkabaj.android.shkabaj.ui.theme.TitleBlueColor
import tk.shkabaj.android.shkabaj.ui.theme.Typography
import tk.shkabaj.android.shkabaj.ui.theme.WhiteColor
import tk.shkabaj.android.shkabaj.utils.DateTimeUtils
import tk.shkabaj.android.shkabaj.utils.Platform
import tk.shkabaj.android.shkabaj.managers.ThemeManager
import tk.shkabaj.android.shkabaj.modules.settings.SettingsAction
import tk.shkabaj.android.shkabaj.navigation.AppScreen
import tk.shkabaj.android.shkabaj.navigation.AppScreenTitle
import tk.shkabaj.android.shkabaj.ui.toolbar.ToolbarAction

class SettingsScreen : AppScreen {

    override val title = AppScreenTitle.Res(Res.string.SettingsTitle)

    override val toolbarActions: List<ToolbarAction>
        get() = emptyList()

    @Composable
    override fun Content() {
        val viewModel = injectScreenModel<SettingsViewModel>()
        val navigator = LocalNavigator.currentOrThrow
        val platform: Platform = koinInject()
        val state by viewModel.viewStates().collectAsState()
        val snackBarHostState = remember { SnackbarHostState() }
        val snackBarMessage = stringResource(Res.string.CacheRemovedMsg)
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            handleToolbarAction { toolbarAction ->
                viewModel.obtainEvent(event = SettingsEvent.OnToolbarEvent(toolbarAction = toolbarAction))
            }
        }

        LaunchedEffect(Unit) {
            viewModel.viewActions().collect { uiAction ->
                when(uiAction) {
                    is SettingsAction.ShowError -> showSnackBar(
                        message = uiAction.error.getErrorMessage(),
                        snackBarHostState = snackBarHostState,
                        coroutine = coroutineScope
                    )
                    is SettingsAction.PopupNavigation -> navigator.pop()
                    else -> {}
                }
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
        ) {
            Box(modifier = Modifier.fillMaxSize().background(color = MainBGColor)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(
                            start = 16.dp,
                            end = 16.dp
                        ),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    SettingsTitle(stringResource(Res.string.Application))
                    SettingsSection(
                        SettingEntity.Home(
                            title = stringResource(Res.string.HomePage),
                            subtitle = state.startTab?.title
                        ),
                        SettingEntity.Theme(
                            title = stringResource(Res.string.SelectTheme),
                            subtitle = state.theme?.title
                        )
                    )
                    SettingCache {
                        viewModel.obtainEvent(event = SettingsEvent.ClearCache)
                        showSnackBar(
                            message = snackBarMessage,
                            snackBarHostState = snackBarHostState,
                            coroutine = coroutineScope
                        )
                    }
                    Footer(
                        version = stringResource(
                            Res.string.AppVersion,
                            platform.getAppVersion()
                        )
                    )
                }
            }
        }
    }

    @Composable
    private fun SettingCache(onCacheClicked: () -> Unit) {
        ElevatedCard(
            colors = CardDefaults.cardColors(
                containerColor = WhiteColor,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize().clickable {
                    onCacheClicked.invoke()
                },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(resource = Res.drawable.ic_recycling),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(Res.string.DeleteCache),
                    style = Typography.bodyLarge.copy(color = BlueColor)
                )
            }
        }
    }

    @Composable
    private fun SettingsTitle(title: String) {
        Box(
            modifier = Modifier.heightIn(44.dp).fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = Afacad,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 28.sp,
                    color = TitleBlueColor
                ),
            )
        }
    }

    @Composable
    private fun SettingsSection(vararg settings: SettingEntity) {
        ElevatedCard(
            colors = CardDefaults.cardColors(
                containerColor = WhiteColor,
            ),
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(4.dp)
        ) {
            Column {
                settings.forEachIndexed { index, setting ->
                    SettingsRowItem(setting)
                    if (index != settings.lastIndex && settings.size > 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            color = DividerNewsColor,
                            thickness = 0.5.dp
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun SettingsRowItem(setting: SettingEntity) {
        val viewModel = injectScreenModel<SettingsViewModel>()
        val bottomSheetNavigator = currentBottomSheetNavigator

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 58.dp)
                .clickable {
                    val dialog = when (setting) {
                        is SettingEntity.Home -> {
                            SettingsStartTabDialog {
                                viewModel.obtainEvent(event = SettingsEvent.OnDefaultScreenChanged)
                                bottomSheetNavigator.hide()
                            }
                        }
                        is SettingEntity.Theme -> SettingsThemeDialog {
                            viewModel.obtainEvent(event = SettingsEvent.OnDefaultThemeChanged)
                        }
                    }
                    dialog?.let(bottomSheetNavigator::show)
                }
                .padding(start = 12.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(resource =
                    if(ThemeManager.isDarkThemeEnabled()) setting.iconResourceDark
                    else setting.iconResourceLight
                ),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )

            Column(
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = setting.title,
                    style = Typography.bodyLarge.copy(color = MaastrichtBlueColor),
                )
                setting.subtitle?.let { subtitle ->
                    Crossfade(subtitle) { newSubtitle ->
                        Text(
                            text = newSubtitle,
                            style = Typography.labelMedium.copy(color = OldSilverColor),
                        )
                    }
                }
            }

            Image(
                painter = painterResource(resource = Res.drawable.ic_chevron_right_24),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(color = RadioPlayerActionColor)
            )
        }
    }

    @Composable
    private fun Footer(version: String) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 36.dp),) {
            Text(
                text = version,
                style = Typography.bodySmall.copy(color = OldSilverColor),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.AllRightsReserved, DateTimeUtils.getCurrentYear().toString()),
                style = Typography.bodySmall.copy(color = OldSilverColor),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
