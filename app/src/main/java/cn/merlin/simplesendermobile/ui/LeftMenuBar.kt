package cn.merlin.simplesendermobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cn.merlin.simplesendermobile.R
import cn.merlin.simplesendermobile.bean.model.DeviceViewModel
import cn.merlin.simplesendermobile.datastore.DSManager
import cn.merlin.simplesendermobile.tools.currentDevice
import coil.compose.SubcomposeAsyncImage

@Composable
fun LeftMenuBar(
    navController: NavController,
    savedDeviceList: SnapshotStateList<DeviceViewModel>,
    dsManager: DSManager
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Column(
        modifier = Modifier
            .padding(start = 16.dp, top = 48.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubcomposeAsyncImage(
            model =
            (
                if(screenWidth <= 600.dp)
                {
                    currentDevice.value.deviceType.value = "phone"
                    R.drawable.phone
                }
                else
                {
                    currentDevice.value.deviceType.value = "laptop"
                    R.drawable.laptop
                }
            ),
            loading = {
                CircularProgressIndicator()
            },
            contentDescription = "",
            modifier = Modifier.size(96.dp)
        )
        Text(text = currentDevice.value.deviceName.value)
        LazyColumn {
            items(savedDeviceList) {
                DeviceCard(it, navController)
            }
        }

    }

}

@Composable
fun DeviceCard(
    deviceViewer: DeviceViewModel,
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