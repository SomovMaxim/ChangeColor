package com.example.changecolor.data.color

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.changecolor.data.RgbColorPreferences
import com.example.changecolor.domain.RgbColor
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RgbColorRepository @Inject constructor(
    @ApplicationContext val context: Context
) {

    companion object {
        private const val RGB_COLOR_PREFERENCES_NAME = "rgb_color.pb"
    }

    private val Context.dataStore: DataStore<RgbColorPreferences> by dataStore(
        RGB_COLOR_PREFERENCES_NAME,
        RgbColorPreferencesSerializer
    )

    val color: Flow<RgbColor> = context.dataStore.data.map { preferences -> preferences.toDomain() }


    suspend fun setColor(color: RgbColor) = context.dataStore.updateData { preferences ->
        preferences.toBuilder()
            .setRed(color.red)
            .setGreen(color.green)
            .setBlue(color.blue)
            .build()
    }

    private fun RgbColorPreferences.toDomain(): RgbColor = RgbColor(red, green, blue)
}