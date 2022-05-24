package com.example.changecolor.presentation.util

import android.graphics.Color
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.changecolor.domain.RgbColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Subscribe on [flow] when [Fragment] is in RESUMED state.
 * Perform [onEach] on every emitted value.
 */
fun <T> Fragment.collectOnLifecycle(
    flow: Flow<T>,
    targetState: Lifecycle.State = Lifecycle.State.RESUMED,
    onEach: (T) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(targetState) {
            flow.collect { onEach(it) }
        }
    }
}

/**
 * Set background color using [RgbColor] values.
 */
fun LinearLayoutCompat.setBackground(color: RgbColor) {
    setBackgroundColor(Color.rgb(color.red, color.green, color.blue))
}