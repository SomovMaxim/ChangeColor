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

    private val firstAttach = MutableStateFlow(true)

    private val _dismiss = Channel<Boolean>(Channel.UNLIMITED)
    val dismiss = _dismiss.receiveAsFlow()

    private var isLeftChecked: Boolean = false
    private var isRightChecked: Boolean = false

    private val redColor = MutableStateFlow<Int?>(0)
    private val greenColor = MutableStateFlow<Int?>(0)
    private val blueColor = MutableStateFlow<Int?>(0)

    val isRedColorValid = redColor.map { ColorValidator.validate(it) }
    val isGreenColorValid = greenColor.map { ColorValidator.validate(it) }
    val isBlueColorValid = blueColor.map { ColorValidator.validate(it) }

    val isSaveButtonEnabled =
        combine(redColor, greenColor, blueColor, firstAttach) { red, green, blue, first ->
            if (first) false
            else {
                val colors = listOf(red, green, blue)
                ColorValidator.validateAll(colors)
            }
        }


    fun onLeftCheckBoxChecked(isChecked: Boolean) {
        isLeftChecked = isChecked
    }

    fun onRightCheckBoxChecked(isChecked: Boolean) {
        isRightChecked = isChecked
    }

    fun onRedColorChanged(value: String) = updateColor(value, redColor)

    fun onGreenColorChanged(value: String) = updateColor(value, greenColor)

    fun onBlueColorChanged(value: String) = updateColor(value, blueColor)

    fun onSaveClicked() = saveData()

    private fun updateColor(colorInString: String, colorToUpdate: MutableStateFlow<Int?>) {
        try {
            val colorInInt = colorInString.toInt()
            colorToUpdate.update { colorInInt }
        } catch (e: Exception) {
            colorToUpdate.update { null }
        } finally {
            firstAttach.update { false }
        }
    }

    private fun saveData() {
        val color = RgbColor(
            red = redColor.value ?: DEFAULT_COLOR_VALUE,
            green = greenColor.value ?: DEFAULT_COLOR_VALUE,
            blue = blueColor.value ?: DEFAULT_COLOR_VALUE
        )

        val settings = PaintSettings(
            isLeftSidePainted = isLeftChecked,
            isRightSidePainted = isRightChecked
        )

        viewModelScope.launch {
            colorRepository.setColor(color)
            settingsRepository.updateSettings(settings)
            _dismiss.trySend(true)
        }
    }
}