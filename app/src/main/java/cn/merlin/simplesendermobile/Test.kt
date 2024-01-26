package cn.merlin.simplesendermobile

import cn.merlin.simplesendermobile.network.SenderServer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    val senderServer = SenderServer()
    val job = senderServer.startServer()
    runBlocking {
        job.join()
    }
}