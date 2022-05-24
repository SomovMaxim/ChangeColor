package com.example.changecolor.presentation.main

import android.graphics.ColorSpace
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.changecolor.data.color.RgbColorRepository
import com.example.changecolor.data.settings.PaintSettingsRepository
import com.example.changecolor.domain.PaintSettings
import com.example.changecolor.domain.RgbColor
import com.example.changecolor.presentation.util.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    colorRepository: RgbColorRepository,
    settingsRepository: PaintSettingsRepository
) : ViewModel() {

    private val defaultColor = RgbColor()

    private val _events = Channel<NavigationEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val savedColor = colorRepository.color
        .stateIn(viewModelScope, SharingStarted.Eagerly, RgbColor())
    private val settings = settingsRepository.settings
        .stateIn(viewModelScope, SharingStarted.Eagerly, PaintSettings())
    val leftSideColor = MutableStateFlow(defaultColor)
    val rightSideColor = MutableStateFlow(defaultColor)

    fun onInputColorClicked() = _events.trySend(NavigationEvent.InputColor)

    fun onChangeClicked() {
        leftSideColor.update {
            if (settings.value.isLeftSidePainted) savedColor.value else defaultColor
        }
        rightSideColor.update {
            if (settings.value.isRightSidePainted) savedColor.value else defaultColor
        }
    }

    fun onHelpClicked() = _events.trySend(NavigationEvent.Help)

    fun onExitClicked() = _events.trySend(NavigationEvent.ExitApplication)
}