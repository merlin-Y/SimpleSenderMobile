package cn.merlin.ui.mainWindow

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cn.merlin.bean.model.DeviceModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DetectPage(
    drawerState: DrawerState,
    scope: CoroutineScope,
    navController: NavController,
    detectedDeviceList: SnapshotStateList<DeviceModel>
){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ){
        Column(
            modifier = Modifier.padding(top = 35.dp, start = 16.dp, end = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(30.dp),
                    shape = MaterialTheme.shapes.extraSmall,
                    color = MaterialTheme.colorScheme.primary,
                    onClick = {
                        scope.launch {
                            delay(100)
                            drawerState.open()
                        }
                    }
                ) {
                    Icon(Icons.Filled.Menu, "",modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp))
                }
                Text("发现设备", modifier = Modifier.padding(start = 126.dp))
            }
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn(
            ) {
                items(detectedDeviceList){
                    Text(text = it.deviceName.value)
                }
            }
        }
    }
}
