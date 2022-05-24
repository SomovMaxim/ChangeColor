package com.example.changecolor.presentation.util

sealed class NavigationEvent {
    object InputColor : NavigationEvent()
    object Help : NavigationEvent()
    object ExitApplication : NavigationEvent()
    object Back : NavigationEvent()
}
