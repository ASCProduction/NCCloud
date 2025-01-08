package tk.shkabaj.android.shkabaj.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import tk.shkabaj.android.shkabaj.managers.ThemeManager

val MainBGLight = Color(0xFFFFFFFF)
val MainBGDark = Color(0xFF08232D)
val MainBGColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) MainBGDark
    else MainBGLight

val AccentLight = Color(0xFF2B9CC9)
val AccentDark = Color(0xFF2B9CC9)
val AccentColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) AccentDark
    else AccentLight

val TextWhiteLight = Color(0xFFFBFBFB)
val TextWhiteDark = Color(0xFFFBFBFB)
val TextWhiteColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) TextWhiteDark
    else TextWhiteLight

val TextWhiteWithAlphaLight = Color(0x60FBFBFB)
val TextWhiteWithAlphaDark = Color(0x60FBFBFB)
val TextWhiteWithAlphaColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) TextWhiteWithAlphaDark
    else TextWhiteWithAlphaLight

val TextBlueLight = Color(0xFF60ADD6)
val TextBlueDark = Color(0xFF55B0D4)
val TextBlueColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) TextBlueDark
    else TextBlueLight

val TextSecondBlueLight = Color(0xFF55B0D4)
val TextSecondBlueDark = Color(0xFF55B0D4)
val TextSecondBlueColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) TextSecondBlueDark
    else TextSecondBlueLight

val BlueLight = Color(0xFF53A9CA)
val BlueDark = Color(0xFF53A9CA)
val BlueColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) BlueDark
    else BlueLight

val WeatherBlueLight = Color(0xFF53A9CA)
val WeatherBlueDark = Color(0xFF0E6283)
val WeatherBlueColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) WeatherBlueDark
    else WeatherBlueLight

val BlueWithAlphaLight = Color(0xCC53A9CA)
val BlueWithAlphaDark = Color(0xCC0E6283)
val BlueWithAlphaColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) BlueWithAlphaDark
    else BlueWithAlphaLight

val WhiteLight = Color(0xFFFFFFFF)
val WhiteDark = Color(0xFF043143)
val WhiteColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) WhiteDark
    else WhiteLight

val GreyLight = Color(0xFF868686)
val GreyDark = Color(0xFFD4D4D4)
val GreyColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) GreyDark
    else GreyLight

val DarkGreyLight = Color(0xFF7B7B7B)
val DarkGreyDark = Color(0xFFCBCBCB)
val DarkGreyColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) DarkGreyDark
    else DarkGreyLight

val SearchBarWhiteLight = Color(0xFFFDFDFD)
val SearchBarWhiteDark = Color(0xFF043143)
val SearchBarWhiteColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) SearchBarWhiteDark
    else SearchBarWhiteLight

val RadioTextCardLight = Color(0xFFFDFDFD)
val RadioTextCardDark = Color(0xCC08232D)
val RadioTextCardColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) RadioTextCardDark
    else RadioTextCardLight

val OldSilverLight = Color(0xFF868686)
val OldSilverDark = Color(0xFFD4D4D4)
val OldSilverColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) OldSilverDark
    else OldSilverLight

val BorderLight = Color(0xFF868686)
val BorderDark = Color(0xFF868686)
val BorderColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) BorderDark
    else BorderLight

val MaastrichtBlueLight = Color(0xFF08232D)
val MaastrichtBlueDark = Color(0xFFFBFBFB)
val MaastrichtBlueColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) MaastrichtBlueDark
    else MaastrichtBlueLight

val TitleBlueLight = Color(0xFF08232D)
val TitleBlueDark = Color(0xFFCBCBCB)
val TitleBlueColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) TitleBlueDark
    else TitleBlueLight

val SliderBoxLight = Color(0xFFCBCBCB)
val SliderBoxDark = Color(0xFFCBCBCB)
val SliderBoxColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) SliderBoxDark
    else SliderBoxLight

val SubTitleBlueLight = Color(0xFF08232D)
val SubTitleBlueDark = Color(0xFFD4D4D4)
val SubTitleColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) SubTitleBlueDark
    else SubTitleBlueLight

val CulturedGreyLight = Color(0xFFF8F8F8)
val CulturedGreyDark = Color(0xFF043143)
val CulturedGreyColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) CulturedGreyDark
    else CulturedGreyLight

val DividerColorLight = Color(0xFFD4D4D4)
val DividerColorDark = Color(0xFFFBFBFB)
val DividerColorColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) DividerColorDark
    else DividerColorLight

val DividerNewsLight = Color(0xFFD4D4D4)
val DividerNewsDark = Color(0xFFD4D4D4)
val DividerNewsColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) DividerNewsDark
    else DividerNewsLight

val ListeningCountLight = Color(0xFF7B7B7B)
val ListeningCountDark = Color(0xFFD4D4D4)
val ListeningCountColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) ListeningCountDark
    else ListeningCountLight

val LightCobaltBlueLight = Color(0xFF8DA4DB)
val LightCobaltBlueDark = Color(0xCC49569D)
val LightCobaltBlueColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) LightCobaltBlueDark
    else LightCobaltBlueLight

val CarolinaBlueLight = Color(0x3355ABCB)
val CarolinaBlueDark = Color(0xFF043143)
val CarolinaBlueColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) CarolinaBlueDark
    else CarolinaBlueLight

val WeatherMainCardLight = Color(0x3355ABCB)
val WeatherMainCardDark = Color(0x4D0E6283)
val WeatherMainCardColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) WeatherMainCardDark
    else WeatherMainCardLight

val OrangeLight = Color(0xE6FFB96C)
val OrangeDark = Color(0xFFFFB462)
val OrangeColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) OrangeDark
    else OrangeLight

val OrangeWeatherLight = Color(0xFFFFB462)
val OrangeWeatherDark = Color(0xFFFFAA4E)
val OrangeWeatherColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) OrangeWeatherDark
    else OrangeWeatherLight

val LinkGreenLight = Color(0xFF5BCF87)
val LinkGreenDark = Color(0xFF3EA163)
val LinkGreenColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) LinkGreenDark
    else LinkGreenLight

val LinkBlueLight = Color(0xFF8E9CEE)
val LinkBlueDark = Color(0xFF49569D)
val LinkBlueColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) LinkBlueDark
    else LinkBlueLight

val TextBGLight = Color(0xCCF8F8F8)
val TextBGDark = Color(0xCC043143)
val TextBGColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) TextBGDark
    else TextBGLight

val TextBGDurationLight = Color(0xCC043143)
val TextBGDurationDark = Color(0xCC043143)
val TextBGDurationColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) TextBGDurationDark
    else TextBGDurationLight


val BarMinimumLight = Color(0xFFFBD316)
val BarMinimumDark = Color(0xFFFBD316)
val BarMinimumColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) BarMinimumDark
    else BarMinimumLight

val BarMaximumLight = Color(0xFFF97700)
val BarMaximumDark = Color(0xFFF97700)
val BarMaximumColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) BarMaximumDark
    else BarMaximumLight

val DotColorLight = Color(0xFF0E6283)
val DotColorDark = Color(0xFF0E6283)
val DotColorColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) DotColorDark
    else DotColorLight

val SettingsIconLight = Color(0xFF043143)
val SettingsIconDark = Color(0xFFD4D4D4)
val SettingsIconColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) SettingsIconDark
    else SettingsIconLight

val ToolbarIconLight = Color(0xFF08232D)
val ToolbarIconDark = Color(0xFFFBFBFB)
val ToolbarIconColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) ToolbarIconDark
    else ToolbarIconLight

val BlackTextLight = Color(0xFF000000)
val BlackTextDark = Color(0xFFFBFBFB)
val BlackTextColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) BlackTextDark
    else BlackTextLight

val OpacityBGLight = Color(0x33F8F8F8)
val OpacityBGDark = Color(0x6608232D)
val OpacityBGColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) OpacityBGDark
    else OpacityBGLight

val RadioPlayerActionLight = Color(0xFF043143)
val RadioPlayerActionDark = Color(0xFFFBFBFB)
val RadioPlayerActionColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) RadioPlayerActionDark
    else RadioPlayerActionLight

val TemperatureMinLight = Color(0xFFE8EAED)
val TemperatureMinDark = Color(0xFFE8EAED)
val TemperatureMinColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) TemperatureMinDark
    else TemperatureMinLight

val TVNewsCardBGLight = Color(0xFFFFFFFF)
val TVNewsCardBGDarkStart = Color(0xFF072936)
val TVNewsCardBGDarkEnd = Color(0xFF0D4F68)
val TVNewsCardColor
    @Composable
    get() = if(ThemeManager.isDarkThemeEnabled()) {
        Brush.horizontalGradient(
            colors = listOf(TVNewsCardBGDarkStart, TVNewsCardBGDarkEnd)
        )
    } else {
        Brush.linearGradient(listOf(TVNewsCardBGLight, TVNewsCardBGLight))
    }

val ShadowLight = Color(0xFF000000)
val ShadowDark = Color(0xFFFFFFFF)
val ShadowColor
    @Composable
    get() = if (ThemeManager.isDarkThemeEnabled()) ShadowDark
    else ShadowLight

val OnlyWhite = Color(0xFFFFFFFF)

val CardButtonBG = Color(0x26878787)
val LightBlueBG = Color(0xFFDDEEF5)
val LoadingCircleColor = Color(0xFF00BAF2)
val RedColor = Color(0xFFE41E20)