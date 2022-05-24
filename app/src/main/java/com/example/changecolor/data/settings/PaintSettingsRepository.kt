package com.example.changecolor.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.changecolor.data.PaintSettingsPreferences
import com.example.changecolor.domain.PaintSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@Singleton
class PaintSettingsRepository(@ApplicationContext val context: Context) {

    companion object {
        private const val PAINT_SETTINGS_PREFERENCES_NAME = "paint_settings.pb"
    }

    private val Context.dataStore: DataStore<PaintSettingsPreferences> by dataStore(
        PAINT_SETTINGS_PREFERENCES_NAME,
        PaintSettingsPreferencesSerializer
    )

    val settings: Flow<PaintSettings> = context.dataStore.data.map { prefs -> prefs.toDomain() }


    suspend fun updateSettings(settings: PaintSettings) = context.dataStore.updateData { prefs ->
        prefs.toBuilder()
            .setIsLeftSidePainted(settings.isLeftSidePainted)
            .setIsRightSidePainted(settings.isRightSidePainted)
            .build()
    }

    private fun PaintSettingsPreferences.toDomain(): PaintSettings = PaintSettings(
        isLeftSidePainted = this.isLeftSidePainted,
        isRightSidePainted = this.isRightSidePainted
    )
}