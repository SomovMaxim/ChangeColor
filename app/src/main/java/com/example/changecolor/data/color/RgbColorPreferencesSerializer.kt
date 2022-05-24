@file:Suppress("BlockingMethodInNonBlockingContext")

package com.example.changecolor.data.color

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.changecolor.data.RgbColorPreferences
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object RgbColorPreferencesSerializer : Serializer<RgbColorPreferences> {
    override val defaultValue: RgbColorPreferences
        get() = RgbColorPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): RgbColorPreferences {
        try {
            return RgbColorPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: RgbColorPreferences, output: OutputStream) = t.writeTo(output)
}