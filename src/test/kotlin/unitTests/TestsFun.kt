package unitTests

import chain.Block
import chain.Node

val TestBlock = Block(12, "12", "12", "12", 12, false)

fun Node.genBlock(): Block {
    var stepBlock: Block? = null
    while (stepBlock == null) {
        stepBlock = this.attemptMakeCorrectBlock()
    }
    return stepBlock
}