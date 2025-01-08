package tk.shkabaj.android.shkabaj.ui.toolbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.ic_toolbar_logo
import nccloud.composeapp.generated.resources.ic_back_navigation
import tk.shkabaj.android.shkabaj.ui.customcomponents.CustomAnimationPlaying
import tk.shkabaj.android.shkabaj.ui.theme.CulturedGreyColor
import tk.shkabaj.android.shkabaj.ui.theme.MaastrichtBlueColor
import tk.shkabaj.android.shkabaj.ui.theme.MainBGColor
import tk.shkabaj.android.shkabaj.ui.theme.SettingsIconColor
import tk.shkabaj.android.shkabaj.ui.theme.ToolbarIconColor
import tk.shkabaj.android.shkabaj.navigation.AppScreen
import tk.shkabaj.android.shkabaj.navigation.ToolbarActionHelper
import tk.shkabaj.android.shkabaj.player.AudioPlayerState
import tk.shkabaj.android.shkabaj.player.NowPlayingState
import tk.shkabaj.android.shkabaj.ui.crypto.CryptoScreen
import tk.shkabaj.android.shkabaj.ui.main.MainScreen
import tk.shkabaj.android.shkabaj.ui.news.NewsScreen
import tk.shkabaj.android.shkabaj.ui.theme.TitleBlueColor
import tk.shkabaj.android.shkabaj.ui.weather.main.WeatherScreen
import tk.shkabaj.android.shkabaj.utils.Constants

@Composable
fun ToolbarLayout(toolbarManager: ToolbarManager) {
    val screen by toolbarManager.currentScreen.collectAsState()
    val isShowingFilterNews by toolbarManager.showNewsFilter.collectAsState()
    val nowPlayingState by toolbarManager.nowPlayingState.collectAsState()

    val isRoot = when(screen) {
        is MainScreen, is NewsScreen, is CryptoScreen, is WeatherScreen  -> true
        else -> false
    }

    if (isRoot) {
        RootToolbarLayout(screen = screen, isAudioPlaying = nowPlayingState.playerState != AudioPlayerState.STOP, isShowingFilterNews = isShowingFilterNews)
    } else {
        ChildToolbarTest(screen = screen)
    }
}

@Composable
private fun RootToolbarLayout(screen: AppScreen?, isAudioPlaying: Boolean, isShowingFilterNews: Boolean) {
    Box(modifier = Modifier.height(height = Constants.TOOLBAR_HEIGHT.dp).fillMaxWidth()) {
        Row(
            modifier = Modifier.height(height = Constants.TOOLBAR_HEIGHT.dp - 1.dp).fillMaxWidth()
                .background(color = MainBGColor)
        ) {
            Spacer(
                modifier = Modifier.weight(weight = 1f)
            )
            if (isAudioPlaying) {
                CustomAnimationPlaying(
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                ) {
                    ToolbarActionHelper.sendToolbarAction(action = ToolbarAction.PLAYER)
                }
            }
            screen?.toolbarActions?.forEach { toolbarAction ->
                when(toolbarAction) {
                    ToolbarAction.FILTER -> {
                        if (isShowingFilterNews) {
                            Image(
                                painter = painterResource(resource = toolbarAction.icon),
                                contentDescription = null,
                                modifier = Modifier.padding(end = 16.dp)
                                    .align(alignment = Alignment.CenterVertically)
                                    .clickable { ToolbarActionHelper.sendToolbarAction(toolbarAction) },
                                colorFilter = ColorFilter.tint(ToolbarIconColor)
                            )
                        }
                    }
                    else -> {
                        Image(
                            painter = painterResource(resource = toolbarAction.icon),
                            contentDescription = null,
                            modifier = Modifier.padding(end = 16.dp)
                                .align(alignment = Alignment.CenterVertically)
                                .clickable { ToolbarActionHelper.sendToolbarAction(toolbarAction) },
                            colorFilter = ColorFilter.tint(ToolbarIconColor)
                        )
                    }
                }
            }
        }
        Text(
            modifier = Modifier.align(alignment = Alignment.Center),
            text = "NCCloud",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TitleBlueColor
        )
        Box(
            modifier = Modifier.fillMaxWidth().height(height = 1.dp)
                .background(color = if (screen is NewsScreen) MainBGColor else CulturedGreyColor)
                .align(alignment = Alignment.BottomCenter)
        )
    }
}

@Composable
private fun ChildToolbarTest(screen: AppScreen?) {
    Box(modifier = Modifier.height(height = Constants.TOOLBAR_HEIGHT.dp).fillMaxWidth()) {
        Row(
            modifier = Modifier.height(height = Constants.TOOLBAR_HEIGHT.dp - 1.dp).fillMaxWidth()
                .background(color = MainBGColor)
        ) {
            Image(
                painter = painterResource(resource = Res.drawable.ic_back_navigation),
                contentDescription = null,
                modifier = Modifier.padding(start = 24.dp)
                    .align(alignment = Alignment.CenterVertically)
                    .clickable { ToolbarActionHelper.sendToolbarAction(ToolbarAction.BACK_NAVIGATION) },
                colorFilter = ColorFilter.tint(SettingsIconColor)
            )
            Spacer(modifier = Modifier.weight(weight = 1f))
            Text(
                text = screen?.title?.getString() ?: "",
                color = MaastrichtBlueColor,
                fontSize = 16.sp,
                maxLines = 1,
                modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    .padding(end = 32.dp)
            )
            Spacer(modifier = Modifier.weight(weight = 1f))
            screen?.toolbarActions?.forEach {
                Image(
                    painter = painterResource(resource = it.icon),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 16.dp)
                        .align(Alignment.CenterVertically)
                        .clickable { ToolbarActionHelper.sendToolbarAction(it) },
                    colorFilter = ColorFilter.tint(color = SettingsIconColor)
                )
            }

        }
        Box(
            modifier = Modifier.fillMaxWidth().height(height = 1.dp)
                .background(color = CulturedGreyColor)
                .align(alignment = Alignment.BottomCenter)
        )
    }
}