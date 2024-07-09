package cn.nerlin.simplesendermobile.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val isCustomTheme = booleanPreferencesKey("isCustomTheme")
    val isInDarkTheme = booleanPreferencesKey("isInDarkTheme")
}