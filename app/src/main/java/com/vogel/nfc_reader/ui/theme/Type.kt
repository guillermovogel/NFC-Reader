package com.vogel.nfc_reader.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(

    h1 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        color = Color.White,
    ),


    h3 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = Color.White,
    ),

    h4 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = Color.White,
    ),

    h6 = TextStyle(
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
        color = Color.White,
    ),

    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        color = Color.White,
    ),

    body2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color.White,
    ),


    caption = TextStyle(
        fontSize = 18.sp,
        letterSpacing = 3.sp,
        color = Color.White,
    ),
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)
// endregion