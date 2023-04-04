package processes

import java.net.Socket

data class Proc(val procName: String, val port: Int) {
    private lateinit var serverSocket: Socket
    private lateinit var clientSocket: Socket

    private var serverThread: Thread? = null
    private var clientThread: Thread? = null

    private val clientSockets: MutableList<Socket> = mutableListOf()
    private val clientThreads: MutableList<Thread> = mutableListOf()

    fun stop() {
        clientThreads.forEach { it.interrupt() }
        clientThreads.clear()

        serverThread?.interrupt()

        clientSockets.forEach { it.close() }
        serverSocket.close()

    }
}