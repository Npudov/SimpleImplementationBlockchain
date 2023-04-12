package chain

import java.lang.StringBuilder
import kotlin.random.Random.Default.nextInt
import java.security.MessageDigest

open class Node {
    private val LENGTH_HASH = 256
    private val NONCE_DEF_VALUE = 0L

    private var genBlock = Block(
        index=0,
        data="asba",
        previousHash="last",
        currentHash="09a4b2272c88fa4fe0b6742f030f516430c16c672799162f1b425471ecdb996c",
        nonce=NONCE_DEF_VALUE,
        isActual=false
    )

    private var lastBlock: Block = genBlock
    var stepBlock: Block? = null

    fun getGenBlock(): Block {
        return genBlock
    }

    fun setGenBlock(index:Long, data: String, prevHash: String, curHash: String, nonce: Long, isAct: Boolean) {
        genBlock = Block(
            index=index,
            data=data,
            previousHash=prevHash,
            currentHash=curHash,
            nonce=nonce,
            isActual=isAct
        )
    }

    fun getLastBlock(): Block {
        return lastBlock
    }

    fun attemptMakeCorrectBlock(): Block? {
        lateinit var currentHash: String
        if (stepBlock == null) {
            val index = lastBlock.index + 1
            val data = getBlockData()
            val previousHash = lastBlock.currentHash
            val nonce = NONCE_DEF_VALUE
            currentHash = getHashBlock(ind=index, lastHash=previousHash, data=data, nonce=nonce)
            stepBlock = Block(
                index=lastBlock.index + 1,
                data=getBlockData(),
                previousHash=lastBlock.currentHash,
                currentHash=currentHash,
                nonce=NONCE_DEF_VALUE
            )
        } else {
            stepBlock?.let {
                currentHash = getHashBlock(
                    ind=it.index, lastHash=it.previousHash, data=it.data, nonce=it.nonce + 1
                )
                stepBlock = it.copy(currentHash=currentHash, nonce=it.nonce+1)
                /*stepBlock = Block(
                    index=it.index,
                    data=it.data,
                    previousHash=it.previousHash,
                    currentHash=currentHash,
                    nonce=it.nonce + 1
                )*/
            }
        }
        return if (currentHash.validateHash()) {
            stepBlock?.also { lastBlock = it; stepBlock = null}
        } else {
            null
        }
    }

    fun setLastBlock(block: Block) {
        lastBlock = block
        stepBlock = null
    }

    fun isCorrectBlock(block: Block): Boolean {
        return block.currentHash.validateHash() && block.index == lastBlock.index + 1
    }

    protected fun getHashBlock(ind: Long, lastHash: String, data: String, nonce: Long): String {
        return (ind.toString() + lastHash + data + nonce.toString()).toSHA()
    }

    protected fun getBlockData(): String {
        return generateRandomData(LENGTH_HASH)
    }

    private fun generateRandomData(length: Int) : String {
        val symbols: List<Char> = ('A'.. 'Z') + ('a'.. 'z') + ('0'..'9')
        val sb: StringBuilder = StringBuilder()
        for (i in 0 until length) {
            val randIndex = nextInt(symbols.size)
            sb.append(symbols[randIndex])
        }
        return sb.toString()
    }

    private fun String.validateHash(): Boolean {
        return this.takeLast(4) == "0000"
    }

    private fun String.toSHA(): String {
        val bytes: ByteArray = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
        return bytes.toHex()
    }

    private fun ByteArray.toHex(): String {
        return joinToString("") {"%02x".format(it)}
    }
}