package processes

const val FIRST_PORT = 8001
const val SECOND_PORT = 8002
const val THIRD_PORT = 8003
const val LOCAL_HOST = "localhost"

fun getNumberNode(port: Int): Int {
    return port % 1000
}

fun getGroupPorts(port: Int): List<Int> {
    return if (port == FIRST_PORT) {
        listOf(SECOND_PORT, THIRD_PORT)
    } else if (port == SECOND_PORT) {
        listOf(FIRST_PORT, THIRD_PORT)
    }
    else {
        listOf(FIRST_PORT, SECOND_PORT)
    }
}

fun isHighLevelProc(proc: Proc):Boolean {
    return proc.numberNode == getNumberNode(FIRST_PORT)
}

