package com.example.changecolor.domain

data class RgbColor(
    val red: Int = DEFAULT_COLOR_VALUE,
    val green: Int = DEFAULT_COLOR_VALUE,
    val blue: Int = DEFAULT_COLOR_VALUE
) {
    companion object {
        const val DEFAULT_COLOR_VALUE = 0
    }
}
