package cn.nerlin.simplesendermobile.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.NavController
import cn.nerlin.simplesendermobile.bean.beanviewers.DeviceViewer
import cn.nerlin.simplesendermobile.datastore.DSManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Detect(
    drawerState: DrawerState,
    scope: CoroutineScope,
    navController: NavController,
    savedDeviceList: SnapshotStateList<DeviceViewer>,
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
                    modifier = Modifier.size(30.dp).weight(1f),
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
            ) {
                items(savedDeviceList){
                    Text(text = it.deviceName.value)
                }
            }
        }
    }
}