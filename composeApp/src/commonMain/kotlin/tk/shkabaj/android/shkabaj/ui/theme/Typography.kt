package tk.shkabaj.android.shkabaj.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import nccloud.composeapp.generated.resources.Res
import nccloud.composeapp.generated.resources.afacad_bold
import nccloud.composeapp.generated.resources.afacad_medium
import nccloud.composeapp.generated.resources.afacad_regular
import nccloud.composeapp.generated.resources.afacad_semibold
import nccloud.composeapp.generated.resources.sfprotext_bold
import nccloud.composeapp.generated.resources.sfprotext_light
import nccloud.composeapp.generated.resources.sfprotext_regular
import nccloud.composeapp.generated.resources.sfprotext_semibold
import nccloud.composeapp.generated.resources.sfrotext_medium

val SFProText: FontFamily
    @Composable
    get() = FontFamily(
        Font(resource = Res.font.sfprotext_light, weight = FontWeight.Light),
        Font(resource = Res.font.sfprotext_regular, weight = FontWeight.Normal),
        Font(resource = Res.font.sfrotext_medium, weight = FontWeight.Medium),
        Font(resource = Res.font.sfprotext_semibold, weight = FontWeight.SemiBold),
        Font(resource = Res.font.sfprotext_bold, weight = FontWeight.Bold)
    )

val Afacad: FontFamily
    @Composable
    get() = FontFamily(
        Font(resource = Res.font.afacad_regular, weight = FontWeight.Normal),
        Font(resource = Res.font.afacad_medium, weight = FontWeight.Medium),
        Font(resource = Res.font.afacad_semibold, weight = FontWeight.SemiBold),
        Font(resource = Res.font.afacad_bold, weight = FontWeight.Bold)
    )

val Typography: Typography
    @Composable
    get() = Typography(
        // H1 in figma
        headlineLarge = TextStyle(
            fontFamily = SFProText,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            lineHeight = 24.sp
        ),
        // H2 in figma
        headlineMedium = TextStyle(
            fontFamily = SFProText,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            lineHeight = 25.sp
        ),
        // H3 in figma
        headlineSmall = TextStyle(
            fontFamily = SFProText,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 25.sp
        ),
        // Text 16 regular in figma
        bodyLarge = TextStyle(
            fontFamily = SFProText,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp
        ),
        // Text 15 regular in figma
        bodyMedium = TextStyle(
            fontFamily = SFProText,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            lineHeight = 18.sp
        ),
        // Text 14 regular in figma
        bodySmall = TextStyle(
            fontFamily = SFProText,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
        ),
        // Text 13 regular in figma
        labelLarge = TextStyle(
            fontFamily = SFProText,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp
        ),
        // Text 12 regular in figma
        labelMedium = TextStyle(
            fontFamily = SFProText,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        ),
        labelSmall = TextStyle(
            fontFamily = SFProText,
            fontWeight = FontWeight.Normal,
            fontSize = 10.sp,
            lineHeight = 15.sp,
        ),
    )