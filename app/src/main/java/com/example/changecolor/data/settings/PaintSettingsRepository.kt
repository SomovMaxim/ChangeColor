package com.example.changecolor.data.settings

import androidx.datastore.core.DataStore
import com.example.changecolor.data.PaintSettingsPreferences
import com.example.changecolor.domain.PaintSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@Singleton
class PaintSettingsRepository(private val dataStore: DataStore<PaintSettingsPreferences>) {

    val settings: Flow<PaintSettings> = dataStore.data.map { prefs -> prefs.toDomain() }


    suspend fun updateSettings(settings: PaintSettings) = dataStore.updateData { preferences ->
        preferences.toBuilder()
            .setIsLeftSidePainted(settings.isLeftSidePainted)
            .setIsRightSidePainted(settings.isRightSidePainted)
            .build()
    }

    private fun PaintSettingsPreferences.toDomain(): PaintSettings = PaintSettings(
        isLeftSidePainted = this.isLeftSidePainted,
        isRightSidePainted = this.isRightSidePainted
    )
}