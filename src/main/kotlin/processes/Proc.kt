package processes

import BlockJsonObject
import chain.Block
import com.google.gson.JsonParseException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException

data class Proc(val procName: String, val port: Int) {
    private lateinit var serverSocket: ServerSocket
    private lateinit var clientSocket: Socket

    private var serverThread: Thread? = null
    private var clientThread: Thread? = null

    private val listClientSockets: MutableList<Socket> = mutableListOf()
    private val listClientThreads: MutableList<Thread> = mutableListOf()


    fun startProcesses() {
        //open server socket for listen incoming connections from clients
        serverSocket = ServerSocket(port)

        //open thread for incoming connections
        serverThread = Thread {
            while (!Thread.currentThread().isInterrupted) {
                try {
                    Thread.sleep(1000)

                    // make connection client with server
                    clientSocket = serverSocket.accept()
                    listClientSockets.add(clientSocket)

                    clientThread = Thread {
                        val inData = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
                        while (!Thread.currentThread().isInterrupted) {
                            try {
                                Thread.sleep(1000)
                                val str = inData.readLine()
                                try {
                                    val blockchainBlock = BlockJsonObject.gsonObj.fromJson(str, Block::class.java) ?: break
                                    BlockJsonObject.block = blockchainBlock
                                    if (blockchainBlock.isActual) {
                                        println("$procName block actualize: $blockchainBlock")
                                    } else {
                                        println("$procName block receive: $blockchainBlock")
                                    }
                                } catch (e: JsonParseException) {
                                    println("$procName error parse str: $str")
                                }
                            } catch (e: SocketException) {
                                Thread.currentThread().interrupt()
                                println("Error! Client socket closed")
                            } catch (e:InterruptedException) {
                                println("${Thread.currentThread().name} interrupted")
                            }
                        }
                    }
                    clientThread?.start()
                    listClientThreads.add(clientThread!!)

                } catch (e: SocketException) {
                    Thread.currentThread().interrupt()
                    println("Error! Client socket closed")
                } catch (e: InterruptedException) {
                    println("${Thread.currentThread().name} interrupted")
                }
            }
        }
        serverThread?.start()
    }

    fun stopProcesses() {
        listClientThreads.forEach { it.interrupt() }
        listClientThreads.clear()

        serverThread?.interrupt()

        listClientSockets.forEach { it.close() }
        serverSocket.close()

    }
}