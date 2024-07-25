package cn.merlin.simplesendermobile

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cn.merlin.simplesendermobile.bean.model.DeviceViewModel
import cn.merlin.simplesendermobile.datastore.DSManager
import cn.merlin.simplesendermobile.datastore.PreferencesKeys
import cn.merlin.simplesendermobile.network.NetworkController
import cn.merlin.simplesendermobile.tools.currentDevice
import cn.merlin.simplesendermobile.tools.getDeviceName
import cn.merlin.simplesendermobile.tools.updateSettings
import cn.merlin.simplesendermobile.ui.LeftMenuBar
import cn.merlin.simplesendermobile.ui.pages.Detect
import cn.merlin.simplesendermobile.ui.pages.Message
import cn.merlin.simplesendermobile.ui.pages.Settings
import cn.merlin.simplesendermobile.ui.theme.SImpleSenderMobileTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val dsManager = DSManager(application)

        setContent {
            val changeTheme = remember { mutableStateOf(0) }
            val isLoading = remember { mutableStateOf(true) }
            val networkJobs: MutableList<Job> = mutableListOf()

            val isInDarkMode = isSystemInDarkTheme()
            val networkController = NetworkController(application,networkJobs)
            getDeviceName(application)

            val useDarkTheme = remember(isInDarkMode) {
                derivedStateOf {
                    when (changeTheme.value) {
                        0 -> isInDarkMode
                        1 -> false
                        2 -> true
                        else -> isInDarkMode
                    }
                }
            }
            val savedDeviceList: SnapshotStateList<DeviceViewModel> =
                remember { mutableStateListOf() }
            val detectedDeviceList: SnapshotStateList<DeviceViewModel> =
                remember { mutableStateListOf() }

            val permissions = mutableListOf(
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.NEARBY_WIFI_DEVICES,
                Manifest.permission.CHANGE_WIFI_STATE
            )
            if (Build.VERSION.SDK_INT == 33) {
                permissions.addAll(
                    listOf(
                        Manifest.permission.READ_MEDIA_AUDIO,
                        Manifest.permission.READ_MEDIA_VIDEO,
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                )
            } else {
                permissions.add(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
            }
            val multiplePermissionsState =
                rememberMultiplePermissionsState(permissions = permissions)

            LaunchedEffect(Unit) {
                multiplePermissionsState.launchMultiplePermissionRequest()
                try {
                    changeTheme.value =
                        dsManager.getIntFlow(PreferencesKeys.changeTheme, 0).first()
                    currentDevice.value.deviceIdentifier.value = dsManager.getStringFlow(PreferencesKeys.identifier,
                        updateSettings(dsManager,PreferencesKeys.identifier.name,UUID.randomUUID().toString())
                    ).first()
                    currentDevice.value.deviceNickName.value = dsManager.getStringFlow(PreferencesKeys.nickName,"").first()
                    isLoading.value = false
                    networkController.startNetworkControl(detectedDeviceList)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            Crossfade(targetState = isLoading.value, label = "Loading animation") {
                if (!multiplePermissionsState.allPermissionsGranted and it) {
                    LoadScreen(
                        text = "获取权限",
                        multiplePermissionsState = multiplePermissionsState
                    )
                } else {
                    SImpleSenderMobileTheme(useDarkTheme) {
                        App(
                            savedDeviceList,
                            detectedDeviceList,
                            dsManager,
                            changeTheme,
                            useDarkTheme,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LoadScreen(text: String, multiplePermissionsState: MultiplePermissionsState) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
        Text(text)
        Button(onClick = { multiplePermissionsState.launchMultiplePermissionRequest() }) {
            Text(text = "request permission")
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun App(
    savedDeviceList: SnapshotStateList<DeviceViewModel>,
    detectedDeviceList: SnapshotStateList<DeviceViewModel>,
    dsManager: DSManager,
    changeTheme: MutableState<Int>,
    useDarkTheme: State<Boolean>,
) {
    /*初始化数据*/
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = listOf(Icons.Default.Favorite, Icons.Default.Face, Icons.Default.Email)
    val selectedItem = remember { mutableStateOf(items[0]) }
    val navController = rememberNavController()


    /*拦截后退键，作用于左边菜单栏*/
    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    /*显示左边菜单栏*/
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet(
            modifier = Modifier
                .shadow(5.dp)
                .width(360.dp),
            drawerShape = MaterialTheme.shapes.medium,
            drawerContainerColor = MaterialTheme.colorScheme.secondary,
        ) {
            LeftMenuBar(navController, savedDeviceList, dsManager)
        }
    }) {
        NavHost(
            navController = navController,
            startDestination = "detect"
        ) {
            composable(route = "detect") {
                Detect(drawerState, scope, navController, savedDeviceList,detectedDeviceList)
            }
            composable(route = "message") {
                Message()
            }
            composable(route = "settings") {
                Settings()
            }
        }

    }

}

