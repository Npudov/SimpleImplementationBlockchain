package processes

import BlockJsonObject
import java.io.IOException
import java.io.PrintWriter
import java.net.Socket

object Connector {

    var groupSockets = mutableMapOf<Int, Socket>()

    fun Proc.sendInfoToAllProc(msg: Information) {
        for (port in getGroupPorts(this.port)) {
            try {
                val groupSocket = groupSockets.getOrPut(port) {
                    Socket(LOCAL_HOST, port)
                }

                val strOnOut = PrintWriter(groupSocket.getOutputStream(), true)
                strOnOut.println(BlockJsonObject.gsonObj.toJson(msg))
            } catch (e: IOException) {
                println("Can't connect to group on port $port")
            }
        }
    }
}