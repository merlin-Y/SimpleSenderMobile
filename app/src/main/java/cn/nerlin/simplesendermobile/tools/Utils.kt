package cn.nerlin.simplesendermobile.tools

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.dataStore
import cn.nerlin.simplesendermobile.bean.Device
import cn.nerlin.simplesendermobile.bean.beanviewers.DeviceViewer
import cn.nerlin.simplesendermobile.datastore.DSManager
import cn.nerlin.simplesendermobile.datastore.PreferencesKeys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last

val currentDevice = mutableStateOf(DeviceViewer(Device()))
val detectedDeviceIdentifierList: MutableList<String> = mutableListOf()

data class Settings(
    val isCustomTheme: MutableState<Boolean> = mutableStateOf(false),
    val isInDarkTheme: MutableState<Boolean> = mutableStateOf(false)
)

fun getUserProfile(): String {
    return ""
}