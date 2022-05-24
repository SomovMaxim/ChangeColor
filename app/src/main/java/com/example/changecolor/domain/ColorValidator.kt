package com.example.changecolor.domain

object ColorValidator {
    private val MIN_COLOR_VALUE = 0
    private val MAX_COLOR_VALUE = 255

    fun validate(value: Int?): Boolean = value in MIN_COLOR_VALUE..MAX_COLOR_VALUE

    fun validateAll(values: List<Int?>): Boolean {
        return values.all { it in MIN_COLOR_VALUE..MAX_COLOR_VALUE }
    }
}