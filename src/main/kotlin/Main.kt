import BlockJsonObject.getBlock
import chain.Node
import controller.Controller.blockActualToAllController
import controller.Controller.blockReceivedActualController
import controller.Controller.blockReceivedController
import controller.Controller.isBlockBroadcastActual
import processes.Proc
import processes.getNumberNode
import controller.Controller.isBlockReceived
import controller.Controller.isBlockReceivedActual
import controller.Controller.stepBlockController

fun main(args: Array<String>) {
    val port = args[0].toInt()

    val proc = Proc("Process on node with number ${getNumberNode(port)}", getNumberNode(port))

    proc.startProcesses()

    val node = Node()

    while (true) {
        val block = BlockJsonObject.getBlock()

        if (isBlockReceived(block)) {
            blockReceivedController(block!!, node)
        } else if (isBlockReceivedActual(block, proc)) {
            blockReceivedActualController(block!!, node)
        } else if (isBlockBroadcastActual(node, proc)) {
            blockActualToAllController(node, proc)
        } else {
            stepBlockController(node, proc)
        }

    }
    proc.stopProcesses()
}