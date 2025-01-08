package tk.shkabaj.android.shkabaj.ui.weather.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tk.shkabaj.android.shkabaj.extensions.CenteredNonTrim
import tk.shkabaj.android.shkabaj.ui.theme.MaastrichtBlueColor
import tk.shkabaj.android.shkabaj.extensions.replaceDays
import tk.shkabaj.android.shkabaj.extensions.replaceMonths
import tk.shkabaj.android.shkabaj.ui.theme.SFProText
import tk.shkabaj.android.shkabaj.ui.theme.TextSecondBlueColor
import tk.shkabaj.android.shkabaj.ui.theme.Typography

@Composable
fun CityRow(city: String, date: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = city,
            style = Typography.headlineSmall.copy(
                color = TextSecondBlueColor,
                lineHeightStyle = LineHeightStyle.CenteredNonTrim
            ),
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = date.replaceDays().replaceMonths(),
            color = MaastrichtBlueColor,
            fontSize =  15.sp,
            fontFamily = SFProText
        )
    }
}