package cn.merlin.simplesendermobile.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "ssMobile")

class DSManager(context : Context) {
    private val dataStore = context.dataStore

    suspend fun<U> saveData(key: String,value: U){
        when(value){
            is Boolean -> setBooleanValue(booleanPreferencesKey(key), value)
            is String -> setStringValue(stringPreferencesKey(key), value)
            is Int -> setIntValue(intPreferencesKey(key), value)
            is Float -> setFloatValue(floatPreferencesKey(key), value)
            is Long -> setLongValue(longPreferencesKey(key), value)
            else -> throw IllegalArgumentException("This type can not be saved into DataStore.")
        }
    }

    inline fun<reified U> readData(key: String, default: U): Flow<U>{
        val data: Flow<Any> = when(default){
            is Boolean -> getBooleanFlow(booleanPreferencesKey(key), default)
            is String -> getStringFlow(stringPreferencesKey(key), default)
            is Int -> getIntFlow(intPreferencesKey(key), default)
            is Float -> getFloatFlow(floatPreferencesKey(key), default)
            is Long -> getLongFlow(longPreferencesKey(key), default)
            else -> throw IllegalArgumentException("This type can not be saved into DataStore.")
        }
        return data.map {
            it as U
        }
    }

    fun getBooleanFlow(key: Preferences.Key<Boolean>,default : Boolean = false): Flow<Boolean> =
        dataStore.data
            .catch {
                if(it is IOException){
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[key] ?: default
            }

    fun getIntFlow(key: Preferences.Key<Int>,default : Int = 0): Flow<Int> =
        dataStore.data
            .catch {
                if(it is IOException){
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[key] ?: default
            }

    fun getStringFlow(key: Preferences.Key<String>,default : String = ""): Flow<String> =
        dataStore.data
            .catch {
                if(it is IOException){
                    it.printStackTrace()
                    setStringValue(key,default)
                } else {
                    throw it
                }
            }.map {
                it[key] ?: default
            }

    fun getFloatFlow(key: Preferences.Key<Float>,default : Float = 0f): Flow<Float> =
        dataStore.data
            .catch {
                if(it is IOException){
                    it.printStackTrace()
                    setFloatValue(key,default)
                } else {
                    throw it
                }
            }.map {
                it[key] ?: default
            }

    fun getLongFlow(key: Preferences.Key<Long>,default : Long = 0L): Flow<Long> =
        dataStore.data
            .catch {
                if(it is IOException){
                    it.printStackTrace()
                    setLongValue(key,default)
                } else {
                    throw it
                }
            }.map {
                it[key] ?: default
            }

    private suspend fun setBooleanValue(key: Preferences.Key<Boolean>, value: Boolean){
        dataStore.edit {
            it[key] = value
        }
    }

    private suspend fun setStringValue(key: Preferences.Key<String>, value: String){
        dataStore.edit {
            it[key] = value
        }
    }

    private suspend fun setIntValue(key: Preferences.Key<Int>,value: Int){
        dataStore.edit {
            it[key] = value
        }
    }

    private suspend fun setFloatValue(key: Preferences.Key<Float>,value: Float){
        dataStore.edit {
            it[key] = value
        }
    }

    private suspend fun setLongValue(key: Preferences.Key<Long>,value: Long){
        dataStore.edit {
            it[key] = value
        }
    }
}