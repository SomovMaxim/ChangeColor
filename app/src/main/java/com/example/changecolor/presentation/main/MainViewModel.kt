package com.example.changecolor.presentation.main

import androidx.lifecycle.ViewModel
import com.example.changecolor.data.color.RgbColorRepository
import com.example.changecolor.data.settings.PaintSettingsRepository
import com.example.changecolor.presentation.util.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val colorRepository: RgbColorRepository,
    private val settingsRepository: PaintSettingsRepository
) : ViewModel() {

    private val _events = Channel<NavigationEvent>(Channel.UNLIMITED)
    val events = _events.receiveAsFlow()

    fun onInputColorClicked() = _events.trySend(NavigationEvent.InputColor)

    fun onChangeClicked() {

    }

    fun onHelpClicked() = _events.trySend(NavigationEvent.Help)

    fun onExitClicked() = _events.trySend(NavigationEvent.Exit)
}