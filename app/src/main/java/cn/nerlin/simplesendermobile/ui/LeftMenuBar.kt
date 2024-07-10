package cn.nerlin.simplesendermobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cn.nerlin.simplesendermobile.R
import cn.nerlin.simplesendermobile.bean.beanviewers.DeviceViewer
import cn.nerlin.simplesendermobile.datastore.DSManager
import cn.nerlin.simplesendermobile.tools.currentDevice

@Composable
fun LeftMenuBar(
    navController: NavController,
    savedDeviceList: SnapshotStateList<DeviceViewer>,
    dsManager: DSManager
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
            items(savedDeviceList) {
                DeviceCard(it, navController)
            }
        }
//        Text(text = if(isInDarkTheme.value) "In Dark Theme" else "Not In Dark Theme")
    }

}

@Composable
fun DeviceCard(
    deviceViewer: DeviceViewer,
    navController: NavController,
) {
    val text =
        if (deviceViewer.deviceNickName.value != "") deviceViewer.deviceNickName.value else deviceViewer.deviceName.value

    Surface(
        modifier = Modifier
            .padding(top = 10.dp, start = 16.dp)
            .fillMaxWidth()
            .height(52.dp),
        color = MaterialTheme.colorScheme.secondary,
        shape = MaterialTheme.shapes.extraSmall,
        onClick = {
            navController.navigate("message")
            currentDevice.value = deviceViewer
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(
                    when (deviceViewer.deviceType.value) {
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