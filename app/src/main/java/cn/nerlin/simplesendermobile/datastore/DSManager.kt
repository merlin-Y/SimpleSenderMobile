package cn.nerlin.simplesendermobile.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DSManager(context : Context) {
    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "ssMobile")

    private val dataStore = context.dataStore

    suspend fun setBooleanValue(key: Preferences.Key<Boolean>, value: Boolean){
        dataStore.edit {
            it[key] = value
        }
    }

    suspend fun setStringValue(key: Preferences.Key<String>, value: String){
        dataStore.edit {
            it[key] = value
        }
    }

    val isCustomTheme: Flow<Boolean> = context.dataStore.data.map{
        it[PreferencesKeys.isCustomTheme] ?: false
    }
    val isInDarkTheme: Flow<Boolean> = context.dataStore.data.map {
        it[PreferencesKeys.isInDarkTheme] ?: false
    }
}