package cn.merlin.simplesendermobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import cn.merlin.simplesendermobile.ui.theme.SimpleSenderMobileTheme
import cn.merlin.simplesendermobile.network.CurrentDeviceInformation
import cn.merlin.simplesendermobile.network.Sender
import cn.merlin.simplesendermobile.network.SenderServer

class MainActivity : ComponentActivity() {
    val startedServer = mutableStateOf(false)
    val startedScanning = mutableStateOf(false)
    val scanner = Sender()
    val scannerServer = SenderServer()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrentDeviceInformation.getDeviceInformation(this)
            SimpleSenderMobileTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column {
                        Row {
                            Button(onClick = {
                                startedServer.value = !startedServer.value
                                scannerServer.startServer()
                            }) {
                                Text(text = "Start Server")
                            }
                            Button(onClick = { 
                                startedScanning.value = !startedScanning.value
                                scanner.detectDevices()
                            }) {
                                Text(text = "Start Scanning")
                            }
                        }
                        Row {
                            if(startedServer.value){
                                Text(text = "Server Started")
                            }
                            if(startedScanning.value){
                                Text(text = "Scanning Started")
                            }
                        }
                    }
                }
            }
        }
    }
}