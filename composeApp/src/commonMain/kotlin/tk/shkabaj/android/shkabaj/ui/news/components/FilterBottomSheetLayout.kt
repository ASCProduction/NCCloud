package tk.shkabaj.android.shkabaj.ui.news.components

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
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import nccloud.composeapp.generated.resources.Apply
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.ic_baseline_check_24
import tk.shkabaj.android.shkabaj.ui.theme.BlueColor
import tk.shkabaj.android.shkabaj.ui.theme.DarkGreyColor
import tk.shkabaj.android.shkabaj.ui.theme.DividerNewsColor
import tk.shkabaj.android.shkabaj.ui.theme.MaastrichtBlueColor
import tk.shkabaj.android.shkabaj.ui.theme.MainBGColor
import tk.shkabaj.android.shkabaj.ui.theme.OldSilverColor
import tk.shkabaj.android.shkabaj.ui.theme.Typography

class FilterBottomSheetLayout(
    val title: String,
    val items: List<String>,
    val startIndex: Int,
    val closeSheet: (Int) -> Unit
) : Screen {

    @Composable
    override fun Content() {

        var currentType by remember { mutableStateOf(startIndex) }

        Box(
            modifier = Modifier.fillMaxWidth()
                .background(color = MainBGColor)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 16.dp)
            ) {
                HeaderRow { closeSheet.invoke(currentType) }
                ListBlock(
                    currentType = currentType
                ) { index ->
                    currentType = index
                }
            }
        }
    }

    @Composable
    private fun HeaderRow(onApplyClick: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.align(alignment = Alignment.CenterVertically),
                text = title,
                style = Typography.headlineLarge.copy(
                    color = DarkGreyColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier.clickable(onClick = onApplyClick)
                    .align(alignment = Alignment.CenterVertically),
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
        currentType: Int,
        onStringSelected: (Int) -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {

            items.forEachIndexed { index, item ->
                ListItem(text = item, isSelected = index == currentType) {
                    onStringSelected(index)
                }
                HorizontalDivider(
                    color = DividerNewsColor,
                    thickness = 0.5.dp
                )
            }
        }
    }

    @Composable
    private fun ListItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 40.dp)
                .clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = Typography.headlineLarge.copy(
                    color = MaastrichtBlueColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                ),
            )
            Spacer(modifier = Modifier.weight(1f))
            if (isSelected) {
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