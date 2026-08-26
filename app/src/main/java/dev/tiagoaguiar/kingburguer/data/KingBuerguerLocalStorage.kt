package dev.tiagoaguiar.kingburguer.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val USER_CREDENTIALS_NAME = "user_credentials"
private val Context.dataStore by preferencesDataStore(name = USER_CREDENTIALS_NAME)

class KingBurguerLocalStorage(
    context: Context
) {

    private val dataStore: DataStore<Preferences> = context.dataStore

    val userCredentialsFlow = dataStore.data.map { preferences ->
        mapUserCredentials(preferences)
    }

    suspend fun fetchInitialUserCredentials(): UserCredentials{
        val pref = dataStore.data.first().toPreferences()
        val userCredentials = mapUserCredentials(pref)
        return userCredentials
    }

    suspend fun updateUserCredential(userCredentials: UserCredentials) {

        dataStore.edit { preferences ->

            preferences[EXPIRES_TIMESTAMP] =
                System.currentTimeMillis() + (userCredentials.expiresTimestamp * 1000L)
            preferences[ACCESS_TOKEN] = userCredentials.accessToken
            preferences[REFRESH_TOKEN] = userCredentials.refreshToken
            preferences[TOKEN_TYPE] = userCredentials.tokenType

        }
    }

    private fun mapUserCredentials(preferences: Preferences): UserCredentials {
        val expires = preferences[EXPIRES_TIMESTAMP] ?: 0
        val accessToken = preferences[ACCESS_TOKEN] ?: ""
        val refreshToken = preferences[REFRESH_TOKEN] ?: ""
        val tokenType = preferences[TOKEN_TYPE] ?: ""
        return UserCredentials(accessToken, refreshToken, expires, tokenType)
    }

    companion object {
        val EXPIRES_TIMESTAMP = longPreferencesKey("expires_timestamp")
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val TOKEN_TYPE = stringPreferencesKey("token_type")


    }
}