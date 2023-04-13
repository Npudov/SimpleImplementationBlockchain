package chain

import processes.Information

data class Block(
    val index: Long,
    val data: String,
    val previousHash: String,
    val currentHash: String,
    val nonce: Long,
    val isActual: Boolean = false
): Information