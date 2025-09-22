package com.rodrigovalverde.sistema5027.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.rodrigovalverde.sistema5027.R

val urbanistFamily = FontFamily(
    Font(R.font.urbanist_extralight, FontWeight.ExtraLight),
    Font(R.font.urbanist_regular, FontWeight.Normal),
    Font(R.font.urbanist_bold, FontWeight.Bold),
    Font(R.font.urbanist_black, FontWeight.Black),
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = urbanistFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    titleLarge = TextStyle(
        fontFamily = urbanistFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),

    displayLarge = TextStyle(
        fontFamily = urbanistFamily,
        fontWeight = FontWeight.ExtraLight,
        fontSize = 64.sp,
        letterSpacing = (-3).sp
    ),

    headlineLarge = TextStyle(
        fontFamily = urbanistFamily,
        fontWeight = FontWeight.Black,
        fontSize = 36.sp,
        letterSpacing = (-1).sp
    ),

    labelLarge = TextStyle(
        fontFamily = urbanistFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
    ),

/*,
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)