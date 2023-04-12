package unitTests

import chain.Block
import chain.Node
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class NodeTests {
    private val NONCE_DEF_VALUE = 0L

    private lateinit var node: Node


    @BeforeEach
    fun setNode() {
        node = Node()
    }

    @Test
    fun getLastBlock() {
        assertEquals(node.getLastBlock(), node.getGenBlock())
        node.setLastBlock(TestBlock)
        assertEquals(node.getLastBlock(), TestBlock)
    }

    @Test
    fun isValidBlock() {
        val block = node.getGenBlock()
        val newBlock = Block(
            index=1,
            data="asdad",
            previousHash="09a4b2272c88fa4fe0b6742f030f516430c16c672799162f1b425471ecdb996c",
            currentHash="09a4b2272c88fa4fe0b6742f030f516430c16c672799162f1b425471ecdb0000",
            nonce=NONCE_DEF_VALUE,
            isActual=false
        )
        assertTrue(node.isCorrectBlock(newBlock))
        val prevBlock = node.getLastBlock()
        val genBlock = node.genBlock()
        node.setLastBlock(prevBlock)
        assertTrue(node.isCorrectBlock(genBlock))
        assertFalse(node.isCorrectBlock(block))
    }
}