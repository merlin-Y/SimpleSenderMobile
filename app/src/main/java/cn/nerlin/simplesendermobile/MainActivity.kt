package cn.nerlin.simplesendermobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cn.nerlin.simplesendermobile.bean.beanviewers.DeviceViewer
import cn.nerlin.simplesendermobile.datastore.DSManager
import cn.nerlin.simplesendermobile.ui.LeftMenuBar
import cn.nerlin.simplesendermobile.ui.pages.Detect
import cn.nerlin.simplesendermobile.ui.pages.Message
import cn.nerlin.simplesendermobile.ui.theme.SImpleSenderMobileTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val dataStore = DSManager(application)

        setContent {
            val savedDeviceList: SnapshotStateList<DeviceViewer> = remember { mutableStateListOf() }
            val detectedDeviceList: SnapshotStateList<DeviceViewer> = remember { mutableStateListOf() }

            SImpleSenderMobileTheme(dataStore) {
                App(savedDeviceList,detectedDeviceList,dataStore)
            }
        }
    }
}

@Composable
fun App(savedDeviceList: SnapshotStateList<DeviceViewer>, detectedDeviceList: SnapshotStateList<DeviceViewer>, dataStore: DSManager) {
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
            LeftMenuBar(navController, savedDeviceList,dataStore)
        }
    }) {
        NavHost(
            navController = navController,
            startDestination = "detect"
        ) {
            composable(route = "detect") {
                Detect(drawerState, scope, navController, detectedDeviceList)
            }
            composable(route = "message") {
                Message()
            }
            composable(route = "settings") {
                cn.nerlin.simplesendermobile.ui.pages.Settings()
            }
        }

    }

}