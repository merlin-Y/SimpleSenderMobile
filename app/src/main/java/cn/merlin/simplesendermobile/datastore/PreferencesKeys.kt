package cn.merlin.simplesendermobile.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val changeTheme = intPreferencesKey("changeTheme")
    val useDarkTheme = booleanPreferencesKey("useDarkTheme")
}