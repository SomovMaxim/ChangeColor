package com.example.changecolor.data.settings

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.changecolor.data.PaintSettingsPreferences
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object PaintSettingsPreferencesSerializer : Serializer<PaintSettingsPreferences> {
    override val defaultValue: PaintSettingsPreferences
        get() = PaintSettingsPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): PaintSettingsPreferences {
        try {
            return PaintSettingsPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: PaintSettingsPreferences, output: OutputStream) =
        t.writeTo(output)
}