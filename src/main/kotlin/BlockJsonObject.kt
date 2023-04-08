import chain.Block
import com.google.gson.Gson

object BlockJsonObject {
    var block: Block? = null
    var lastActualIndex = 0L
    val gsonObj = Gson()

    fun BlockJsonObject.getBlock(): Block? {
        return this.block.also { this.block = null }
    }
}