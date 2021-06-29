import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val ch = Channel<Int>()

        launch {
            println("ready to receive")
            val value = ch.receive()
            println("received value $value")
        }

        launch {
            println("sleep...")
            delay(3000)
            println("before sending")
            ch.send(42)
            println("after sending")
        }

    }
}
