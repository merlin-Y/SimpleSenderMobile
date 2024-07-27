package cn.merlin.simplesendermobile.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cn.merlin.simplesendermobile.bean.model.DeviceViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Detect(
    drawerState: DrawerState,
    scope: CoroutineScope,
    navController: NavController,
    savedDeviceList: SnapshotStateList<DeviceViewModel>,
    detectedDeviceList: SnapshotStateList<DeviceViewModel>,
    savedDeviceIdentifier: MutableSet<String>
){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ){
        Column(
            modifier = Modifier.padding(top = 35.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.End
            ) {
                Surface(
                    modifier = Modifier
                        .size(30.dp)
                        .weight(1f),
                    shape = MaterialTheme.shapes.extraSmall,
                    color = MaterialTheme.colorScheme.primary,
                    onClick = {
                        scope.launch {
                            delay(100)
                            drawerState.open()
                        }
                    }
                ) {
                    Icon(
                        Icons.Filled.Menu, "",modifier = Modifier
                            .fillMaxSize()
                            .padding(0.dp))
                }
                Text("发现设备", modifier = Modifier.weight(10f), textAlign = TextAlign.End)
            }
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn(
                modifier = Modifier.padding(top = 20.dp, start = 20.dp)
            ) {
                for (device in detectedDeviceList) {
                    item {
                        DeviceCard(device,savedDeviceList, savedDeviceIdentifier)
                    }
                }
            }
        }
    }
}

@Composable
fun DeviceCard(device: DeviceViewModel, savedDeviceList: SnapshotStateList<DeviceViewModel>,savedDeviceIdentifier: MutableSet<String>) {
    Button(
        shape = MaterialTheme.shapes.extraLarge,
        modifier = Modifier
            .size(180.dp)
            .padding(top = 20.dp, start = 20.dp),
        onClick = {
            if(!savedDeviceIdentifier.contains(device.deviceIdentifier.value)){
                device.inListType.value = false
                savedDeviceList.add(device)
            }
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(device.deviceName.value, color = MaterialTheme.colorScheme.onSecondaryContainer, maxLines = 1)
            Text(device.deviceIpAddress.value, color = MaterialTheme.colorScheme.onSecondaryContainer, maxLines = 1)
        }

    }
}