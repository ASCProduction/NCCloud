package tk.shkabaj.android.shkabaj.ui.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.WeatherTitle
import org.jetbrains.compose.resources.stringResource
import tk.shkabaj.android.shkabaj.modules.mainscreen.BallinaWeatherState
import tk.shkabaj.android.shkabaj.ui.theme.CarolinaBlueColor
import tk.shkabaj.android.shkabaj.ui.weather.main.components.MainWeatherInfoCard

@Composable
fun BallinaWeatherCard(
    state: BallinaWeatherState,
    onCityClick: () -> Unit = {},
    onCardClick: () -> Unit = {}
) {
    val selectedCity = state.selectedCity ?: return
    val nowForecast = selectedCity.nowForecast ?: return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CarolinaBlueColor)
            .clickable { onCardClick() }
    ) {

        CardHeader(title = stringResource(Res.string.WeatherTitle), showMore = false)

        MainWeatherInfoCard(
            city = selectedCity,
            forecast = nowForecast,
            drawBackground = false,
            paddingValues = PaddingValues(
                start = 16.dp, end = 16.dp, bottom = 16.dp
            ),
            onClick = onCityClick
        )
    }
}