package cn.merlin

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cn.merlin.bean.model.DeviceModel
import cn.merlin.database.controller.SenderController
import cn.merlin.network.Receiver
import cn.merlin.network.Sender
import cn.merlin.ui.leftMenuBar.LeftMenuBar
import cn.merlin.ui.mainWindow.DetectPage
import cn.merlin.ui.mainWindow.MessagePage
import cn.merlin.ui.mainWindow.SettingsPage
import cn.merlin.ui.theme.SimpleSenderMobileTheme
import com.zy.devicelibrary.UtilsApp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        UtilsApp.init(this.application)
        super.onCreate(savedInstanceState)
        requestPermissions(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_WIFI_STATE,
            android.Manifest.permission.CAMERA
        ) {
            setContent {
                val senderController = SenderController(applicationContext)
                val localDeviceList: SnapshotStateList<DeviceModel> = remember{ mutableStateListOf() }
                val detectedDeviceList: SnapshotStateList<DeviceModel> = remember{ mutableStateListOf() }
                val sender = Sender()
                val receiver = Receiver()
                val isInDarkMode = isSystemInDarkTheme()

                SimpleSenderMobileTheme {
                    LaunchedEffect(Unit) {
                        window.statusBarColor = Color.Transparent.toArgb()
                        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
                            !isInDarkMode
                        senderController.getAllDevice(localDeviceList)
                        receiver.startServer(detectedDeviceList)
                        sender.startScanning()
                    }
                    App(localDeviceList,detectedDeviceList)
                }
            }
        }
    }

    private fun requestPermissions(vararg permissions: String, onResult: (List<String>) -> Unit) {
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val failed = result.filter { !it.value }.keys
            onResult(failed.toList())
        }.launch(arrayOf(*permissions))
    }

}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun App(localDeviceList: SnapshotStateList<DeviceModel>,detectedDeviceList: SnapshotStateList<DeviceModel>) {
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
            drawerShape = MaterialTheme.shapes.extraSmall,
            drawerContainerColor = MaterialTheme.colorScheme.secondary,
        ) {
            LeftMenuBar(navController, localDeviceList)
        }
    }) {
        NavHost(
            navController = navController,
            startDestination = "detect"
        ) {
            composable(route = "detect") {
                DetectPage(drawerState, scope,navController, detectedDeviceList)
            }
            composable(route = "message") {
                MessagePage()
            }
            composable(route = "settings") {
                SettingsPage()
            }
        }

    }

}