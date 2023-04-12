package blockIntegratedTests

import chain.Block
import chain.Node
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import unitTests.genBlock
import kotlin.test.assertEquals

class GenerationTest: Node() { //наследуемся для получения метода getBlockData и getHashBlock для теста
    private lateinit var node: Node


    @BeforeEach
    fun setNode() {
        node = Node()
    }

    @Test
    fun genTest() {
        for (i in 0..9) {
            setGenBlock(index=getRandomDigitLong(),
                data=getBlockData(),
                prevHash=getHashBlock(ind=getRandomDigitLong(), lastHash=getBlockData(), data=getBlockData(), nonce=getRandomDigitLong()),
                curHash=getHashBlock(ind=getRandomDigitLong(), lastHash=getBlockData(), data=getBlockData(), nonce=getRandomDigitLong()),
                nonce=getRandomDigitLong(),
                isAct = false)
            val node = Node()
            assertEquals(node.getLastBlock(), node.getGenBlock())
            for (j in 0..9) {
                val newBlock = node.genBlock()
                assertEquals(node.getLastBlock(), newBlock)
            }
        }
    }

    private fun getRandomDigitLong(): Long {
        return (0..Long.MAX_VALUE).random()
    }
}