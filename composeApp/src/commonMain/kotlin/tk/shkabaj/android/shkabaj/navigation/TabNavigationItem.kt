package tk.shkabaj.android.shkabaj.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import org.jetbrains.compose.resources.painterResource
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.ic_navigation_home_selected
import nccloud.composeapp.generated.resources.ic_navigation_news_selected
import nccloud.composeapp.generated.resources.ic_navigation_crypto
import nccloud.composeapp.generated.resources.ic_navigation_weather_selected
import tk.shkabaj.android.shkabaj.navigation.tabs.TabItem
import tk.shkabaj.android.shkabaj.ui.theme.AccentColor
import tk.shkabaj.android.shkabaj.ui.theme.GreyColor
import tk.shkabaj.android.shkabaj.ui.theme.RedColor
import tk.shkabaj.android.shkabaj.ui.theme.Typography

@Composable
fun RowScope.TabNavigationItem(tab: Tab, currentTabItem: TabItem, showUpdateIndicator: Boolean) {
    val tabNavigator = LocalTabNavigator.current
    val isSelected = tabNavigator.current == tab
    val tint = if (isSelected) AccentColor else GreyColor

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .weight(1f)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() } // This is mandatory
            ) {
                tabNavigator.current = tab
            }
    ) {
        tab.options.icon?.let { icon ->
            Box(modifier = Modifier.width(width = 40.dp)) {
                val needIcon = if(isSelected) when(currentTabItem) {
                    TabItem.HOME -> painterResource(Res.drawable.ic_navigation_home_selected)
                    TabItem.NEWS -> painterResource(Res.drawable.ic_navigation_news_selected)
                    TabItem.WEATHER -> painterResource(Res.drawable.ic_navigation_weather_selected)
                    TabItem.CRYPTO -> painterResource(Res.drawable.ic_navigation_crypto)
                } else icon
                Icon(
                    painter = needIcon,
                    tint = tint,
                    modifier = Modifier.size(28.dp).align(alignment = Alignment.Center),
                    contentDescription = null,
                )
                if(showUpdateIndicator) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(RedColor, shape = CircleShape)
                            .align(Alignment.TopEnd)
                            .offset(x = 0.dp, y = (-8).dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = tab.options.title,
            style = Typography.labelSmall.copy(color = tint),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
