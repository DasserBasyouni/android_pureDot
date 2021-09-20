package com.g7.soft.pureDot.data.serializer

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.g7.soft.pureDot.data.database.UserData
import java.io.InputStream
import java.io.OutputStream

object UserDataSerializer : Serializer<UserData> {
    override val defaultValue: UserData = UserData.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserData {
        try {
            return UserData.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: UserData,
        output: OutputStream
    ) = t.writeTo(output)

}

val Context.userDataStore: DataStore<UserData> by dataStore(
    fileName = "user_data.pb",
    serializer = UserDataSerializer
)