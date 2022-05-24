package com.example.changecolor.presentation.inputColor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.changecolor.data.color.RgbColorRepository
import com.example.changecolor.data.settings.PaintSettingsRepository
import com.example.changecolor.domain.ColorValidator
import com.example.changecolor.domain.PaintSettings
import com.example.changecolor.domain.RgbColor
import com.example.changecolor.domain.RgbColor.Companion.DEFAULT_COLOR_VALUE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class InputColorViewModel @Inject constructor(
    private val colorRepository: RgbColorRepository,
    private val settingsRepository: PaintSettingsRepository
) : ViewModel() {

    private val firstShow = MutableStateFlow(true)

    private val _dismiss = Channel<Boolean>(Channel.UNLIMITED)
    val dismiss = _dismiss.receiveAsFlow()

    private var isLeftChecked: Boolean = false
    private var isRightChecked: Boolean = false

    private val red = MutableStateFlow<Int?>(0)
    private val green = MutableStateFlow<Int?>(0)
    private val blue = MutableStateFlow<Int?>(0)

    val isRedColorValid = red.map { ColorValidator.validate(it) }
    val isGreenColorValid = green.map { ColorValidator.validate(it) }
    val isBlueColorValid = blue.map { ColorValidator.validate(it) }

    val isSaveButtonEnabled = combine(red, green, blue, firstShow) { red, green, blue, first ->
        if (first) false
        else ColorValidator.validateAll(listOf(red, green, blue))
    }

    fun onLeftCheckBoxChecked() {
        isLeftChecked = !isLeftChecked
    }

    fun onRightCheckBoxChecked() {
        isRightChecked = !isRightChecked
    }

    fun onRedColorChanged(value: String) = updateColor(value, red)

    fun onGreenColorChanged(value: String) = updateColor(value, green)

    fun onBlueColorChanged(value: String) = updateColor(value, blue)

    fun onSaveClicked() = saveData()

    private fun updateColor(color: String, colorToUpdate: MutableStateFlow<Int?>) {
        try {
            colorToUpdate.update { color.toInt() }
        } catch (e: Exception) {
            colorToUpdate.update { null }
        } finally {
            firstShow.update { false }
        }
    }

    private fun saveData() {
        val color = RgbColor(
            red = red.value ?: DEFAULT_COLOR_VALUE,
            green = green.value ?: DEFAULT_COLOR_VALUE,
            blue = blue.value ?: DEFAULT_COLOR_VALUE
        )
        val settings = PaintSettings(isLeftChecked, isRightChecked)

        viewModelScope.launch {
            colorRepository.setColor(color)
            settingsRepository.updateSettings(settings)
            _dismiss.trySend(true)
        }
    }
}