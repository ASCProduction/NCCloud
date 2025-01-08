package tk.shkabaj.android.shkabaj.ui.weather.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import tk.shkabaj.android.shkabaj.network.entity.weather.WeatherCity
import tk.shkabaj.android.shkabaj.network.entity.weather.WeatherForecast
import tk.shkabaj.android.shkabaj.ui.theme.DividerNewsColor
import tk.shkabaj.android.shkabaj.ui.theme.MainBGColor
import tk.shkabaj.android.shkabaj.ui.weather.detail.components.CityRow
import tk.shkabaj.android.shkabaj.ui.weather.detail.components.DateRow
import tk.shkabaj.android.shkabaj.ui.weather.detail.components.TopRow
import tk.shkabaj.android.shkabaj.ui.weather.detail.components.WeatherBlock

class WeatherDetailBottomSheet(
    private val city: WeatherCity?,
    private val forecasts: List<WeatherForecast>,
    private val selectedForecast: WeatherForecast? = null,
    private val closeSheet: () -> Unit
) : Screen {

    @Composable
    override fun Content() {
        var currentForecast by remember {
            mutableStateOf(value = selectedForecast ?: forecasts.first())
        }

        Box(modifier = Modifier.fillMaxWidth().background(color = MainBGColor)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                TopRow(closeSheet = closeSheet)

                Spacer(Modifier.height(8.dp))

                DateRow(
                    currentForecast = currentForecast,
                    forecasts = forecasts,
                    onForecastDateSelected = { currentForecast = it }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = DividerNewsColor,
                    thickness = 0.5.dp
                )

                CityRow(
                    city = city?.name ?: "",
                    date = currentForecast.fullDateString
                )

                WeatherBlock(currentForecast)
            }
        }
    }
}