package tk.shkabaj.android.shkabaj.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import nccloud.composeapp.generated.resources.Apply
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.ic_baseline_check_24
import tk.shkabaj.android.shkabaj.ui.customcomponents.CustomSearchBar
import tk.shkabaj.android.shkabaj.ui.theme.BlueColor
import tk.shkabaj.android.shkabaj.ui.theme.DarkGreyColor
import tk.shkabaj.android.shkabaj.ui.theme.DividerColorColor
import tk.shkabaj.android.shkabaj.ui.theme.DividerNewsColor
import tk.shkabaj.android.shkabaj.ui.theme.MaastrichtBlueColor
import tk.shkabaj.android.shkabaj.ui.theme.MainBGColor
import tk.shkabaj.android.shkabaj.ui.theme.OldSilverColor
import tk.shkabaj.android.shkabaj.ui.theme.Typography

class SearchBottomSheet(
    val title: StringResource,
    val list: List<String>,
    var initialString: String,
    val isSearch: Boolean = false,
    val searchTitle: StringResource,
    val closeSheet: (String) -> Unit
) : Screen {

    @Composable
    override fun Content() {
        var currentString by remember { mutableStateOf(initialString) }
        var searchString by remember { mutableStateOf("") }

        val searchQueryChanged: (String) -> Unit = { newQuery ->
            searchString = newQuery
        }

        Box(
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight(0.85f)
                .background(color = MainBGColor)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 4.dp)
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                if (isSearch) {
                    CustomSearchBar(
                        text = searchString,
                        onTextChange = searchQueryChanged,
                        placeHolder = stringResource(searchTitle)
                    )
                }
                HeaderRow { closeSheet.invoke(currentString) }
                ListBlock(
                    currentString = currentString,
                    searchString = searchString
                ) { selectedString ->
                    currentString = selectedString
                }
            }
        }
    }

    @Composable
    private fun HeaderRow(onApplyClick: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(title),
                style = Typography.headlineLarge.copy(
                    color = DarkGreyColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier.clickable(onClick = onApplyClick),
                text = stringResource(Res.string.Apply),
                style = Typography.headlineLarge.copy(
                    color = BlueColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }

    @Composable
    private fun ListBlock(
        currentString: String,
        searchString: String,
        onStringSelected: (String) -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp)
        ) {
            val filteredItems = if (searchString.isBlank()) {
                list
            } else {
                list.filter { it.lowercase().contains(searchString.lowercase()) }
            }

            filteredItems.forEachIndexed { index, text ->
                ListItem(text = text, isSelected = text == currentString) {
                    onStringSelected(text)
                }
                if (index != filteredItems.lastIndex) {
                    HorizontalDivider(
                        color = DividerNewsColor,
                        thickness = 0.5.dp
                    )
                }
            }
        }
    }

    @Composable
    private fun ListItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 40.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onClick
                )
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = Typography.headlineLarge.copy(
                    color = MaastrichtBlueColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn() + expandIn(),
                exit = fadeOut() + shrinkOut()
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_baseline_check_24),
                    contentDescription = "Icon",
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(OldSilverColor),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}