package cn.merlin.ui.leftMenuBar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cn.merlin.R
import cn.merlin.bean.model.DeviceModel
import cn.merlin.tools.currentDevice

@Composable
fun LeftMenuBar(
    navController: NavController,
    localDeviceList: SnapshotStateList<DeviceModel>
) {
    Column {
        Row {
            Spacer(modifier = Modifier.width(26.dp))
            Surface(
                onClick = { /*TODO*/ },
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.extraSmall,
                modifier = Modifier.size(30.dp)
            ) {
                Icon(Icons.Filled.Menu, contentDescription = "", modifier = Modifier.fillMaxSize())
            }
            Spacer(modifier = Modifier.width(50.dp))
            Surface(
                onClick = { /*TODO*/ },
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.extraSmall,
                modifier = Modifier.size(30.dp)
            ) {
                Icon(Icons.Filled.Settings, contentDescription = "", modifier = Modifier.fillMaxSize())
            }
        }
        LazyColumn {
            items(localDeviceList) {
                DeviceCard(it, navController)
            }
        }
    }

}

@Composable
fun DeviceCard(
    deviceModel: DeviceModel,
    navController: NavController,
) {
    val text =
        if (deviceModel.deviceNickName.value != "") deviceModel.deviceNickName.value else deviceModel.deviceName.value

    Surface(
        modifier = Modifier
            .padding(top = 10.dp, start = 16.dp)
            .fillMaxWidth()
            .height(52.dp),
        color = MaterialTheme.colorScheme.secondary,
        shape = MaterialTheme.shapes.extraSmall,
        onClick = {
            navController.navigate("message")
            currentDevice.value = deviceModel
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(
                    when (deviceModel.deviceType.value) {
                        "computer" -> R.drawable.computer
                        "phone" -> R.drawable.phone
                        "laptop" -> R.drawable.laptop
                        else -> R.drawable.phone
                    }
                ),
                contentDescription = text,
                modifier = Modifier.size(50.dp)
            )
            Text(
                text = text,
                modifier = Modifier
                    .padding(start = 10.dp),
                fontSize = 14.sp,
                maxLines = 1
            )
        }
    }
}