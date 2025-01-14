package tk.shkabaj.android.shkabaj.ui.weather.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tk.shkabaj.android.shkabaj.network.entity.weather.WeatherForecast
import tk.shkabaj.android.shkabaj.ui.theme.MaastrichtBlueColor
import tk.shkabaj.android.shkabaj.ui.theme.SFProText
import tk.shkabaj.android.shkabaj.ui.theme.TextSecondBlueColor
import tk.shkabaj.android.shkabaj.ui.theme.TextWhiteColor

@Composable
fun DateRow(
    currentForecast: WeatherForecast,
    forecasts: List<WeatherForecast>,
    onForecastDateSelected: (WeatherForecast) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        forecasts.forEach { forecast ->
            DateItem(
                isCurrent = forecasts.indexOf(forecast) == 0,
                isSelected = forecast == currentForecast,
                modifier = Modifier.weight(1f),
                forecast = forecast,
                onForecastDateSelected = onForecastDateSelected
            )
        }
    }
}

@Composable
private fun DateItem(
    modifier: Modifier = Modifier,
    forecast: WeatherForecast,
    isCurrent: Boolean = false,
    isSelected: Boolean = false,
    onForecastDateSelected: (WeatherForecast) -> Unit = {}
) {
    val textColor = when {
        isCurrent && !isSelected -> TextSecondBlueColor
        isSelected -> TextWhiteColor
        else -> MaastrichtBlueColor
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = forecast.wd ?: "",
            color = MaastrichtBlueColor,
            fontSize =  15.sp,
            fontFamily = SFProText
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(if (isSelected) TextSecondBlueColor else Color.Transparent)
                .clickable { onForecastDateSelected.invoke(forecast) }
        ) {
            Text(
                text = forecast.day ?: "",
                color = textColor,
                fontSize =  15.sp,
                fontFamily = SFProText
            )
        }
    }
}