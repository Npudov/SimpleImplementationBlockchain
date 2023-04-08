package controller

import BlockJsonObject
import chain.Block
import chain.Node
import processes.Connector.sendInfoToAllProc
import processes.Proc
import processes.isHighLevelProc

object Controller {
    const val INTERVAL = 10L


    fun blockReceivedController(block: Block, node: Node): Boolean {
        if (node.isCorrectBlock(block)) {
            node.setLastBlock(block)
            return true
        }
        return false
    }

    fun blockReceivedActualController(block: Block, node: Node): Boolean {
        node.setLastBlock(block)
        return true
    }

    fun stepBlockController(node: Node, proc: Proc): Boolean {
        val stepBlock: Block? = node.attemptMakeCorrectBlock()
        if (stepBlock != null) {
            proc.sendInfoToAllProc(stepBlock)
            return true
        }
        return false
    }

    fun blockActualToAllController(node: Node, proc: Proc): Boolean {
        val blockActual = node.getLastBlock().copy(isActual=true)
        BlockJsonObject.lastActualIndex = blockActual.index
        proc.sendInfoToAllProc(blockActual)
        return true
    }

    fun isBlockReceived(block: Block?): Boolean {
        return block != null && !block.isActual
    }

    fun isBlockReceivedActual(block: Block?, proc: Proc): Boolean {
        return block != null && !isHighLevelProc(proc)
    }

    fun isBlockBroadcastActual(node: Node, proc: Proc): Boolean {
        return BlockJsonObject.lastActualIndex != node.getLastBlock().index
                && node.getLastBlock().index % INTERVAL == 0L
                && isHighLevelProc(proc)
    }
}