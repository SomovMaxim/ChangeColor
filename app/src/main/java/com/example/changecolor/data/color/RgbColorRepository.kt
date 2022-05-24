package com.example.changecolor.data.color

import androidx.datastore.core.DataStore
import com.example.changecolor.data.RgbColorPreferences
import com.example.changecolor.domain.RgbColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@Singleton
class RgbColorRepository(private val dataStore: DataStore<RgbColorPreferences>) {

    val color: Flow<RgbColor> = dataStore.data.map { preferences -> preferences.toDomain() }


    suspend fun setColor(color: RgbColor) = dataStore.updateData { prefs ->
        prefs.toBuilder()
            .setRed(color.red)
            .setGreen(color.green)
            .setBlue(color.blue)
            .build()
    }

    private fun RgbColorPreferences.toDomain(): RgbColor = RgbColor(
        red = this.red,
        green = this.green,
        blue = this.blue
    )
}