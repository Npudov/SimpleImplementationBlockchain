package processTests

import BlockJsonObject
import BlockJsonObject.getBlock
import chain.Node
import controller.Controller.blockReceivedActualController
import controller.Controller.blockReceivedController
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import processes.*
import processes.Connector.sendInfoToAllProc
import unitTests.genBlock
import kotlin.test.assertEquals

class ProcTests {
    private lateinit var firstNode: Node
    private lateinit var secondNode: Node
    private lateinit var thirdNode: Node

    private lateinit var firstProc: Proc
    private lateinit var secondProc: Proc
    private lateinit var thirdProc: Proc

    @BeforeEach
    fun install() {
        firstProc = Proc(procName=getProcName(FIRST_PORT), port=FIRST_PORT)
        secondProc = Proc(procName=getProcName(SECOND_PORT), port=SECOND_PORT)
        thirdProc = Proc(procName=getProcName(THIRD_PORT), port=THIRD_PORT)

        firstProc.startProcesses()
        secondProc.startProcesses()
        thirdProc.startProcesses()

        firstNode = Node()
        secondNode = Node()
        thirdNode = Node()


    }

    @Test
    fun procIsValidBlockTest() {
        firstNode.genBlock()
        secondNode.genBlock()

        val validBlock = secondNode.genBlock()

        secondProc.sendInfoToAllProc(validBlock)
        Thread.sleep(3000)

        blockReceivedController(BlockJsonObject.getBlock()!!, firstNode)
        assertEquals(firstNode.getLastBlock(), validBlock)
    }

    @Test
    fun procIsActualBlockTest() {
        firstNode.genBlock()
        firstNode.genBlock()

        val actualBlock = firstNode.genBlock()

        firstProc.sendInfoToAllProc(actualBlock.copy(isActual=true))
        Thread.sleep(3000)


        blockReceivedActualController(BlockJsonObject.getBlock()!!, secondNode)
        assertEquals(secondNode.getLastBlock(), actualBlock.copy(isActual=true))
    }

    @AfterEach
    fun shutDown() {
        firstProc.stopProcesses()
        secondProc.stopProcesses()
        thirdProc.stopProcesses()
        Thread.sleep(2000)
    }

    private fun getProcName(port: Int): String {
        return "Processing ${getNumberNode(port)}"
    }
}