package com.g7.soft.pureDot.data.database.userData

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.g7.soft.pureDot.data.UserData
import com.g7.soft.pureDot.data.serializer.UserDataSerializer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import timber.log.Timber
import java.io.IOException

class UserDataDatabase(context: Context) {

    private val Context.dataStore: DataStore<UserData> by dataStore(
        fileName = "user_data",
        serializer = UserDataSerializer,
    )

    private val dataStore = context.dataStore


    val userDataFlow: Flow<UserData> = context.dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Timber.e(exception, "Error reading sort order UserData.")
                emit(UserData.getDefaultInstance())
            } else {
                // todo
                throw exception
            }
        }


    suspend fun updateTokenId(value: String?) {
        dataStore.updateData { data -> data.toBuilder().setTokenId(value ?: "").build() }
    }

    suspend fun updateEmailOrPhoneNumber(value: String?) {
        dataStore.updateData { data ->
            data.toBuilder().setEmailOrPhoneNumber(value ?: "").build()
        }
    }

    suspend fun updatePassword(value: String?) {
        dataStore.updateData { data -> data.toBuilder().setPassword(value ?: "").build() }
    }

    suspend fun updateCurrencySymbol(value: String?) {
        dataStore.updateData { data -> data.toBuilder().setCurrencySymbol(value ?: "").build() }
    }

    suspend fun updateIsGuestAccount(value: Boolean?) {
        dataStore.updateData { data -> data.toBuilder().setIsGuestAccount(value ?: false).build() }
    }


    companion object {
        private var INSTANCE: UserDataDatabase? = null

        fun getInstance(context: Context): UserDataDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = UserDataDatabase(context)
                INSTANCE = instance
                instance
            }
        }
    }
}