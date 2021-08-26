package com.g7.soft.pureDot.data.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream

object UserDataSerializer : Serializer<UserData> {
    override val defaultValue: UserData = UserData.getDefaultInstance()

    override fun readFrom(input: InputStream): UserData {
        try {
            return UserData.parseFrom(input)
        } catch (exception: Exception) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override fun writeTo(t: UserData, output: OutputStream) = t.writeTo(output)

}