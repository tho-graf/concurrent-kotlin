import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
//        crashingThreadExample()

        val channels = (0..1_000_000).map { Channel<Int>() }
        val first = channels.first()
        val last = channels.last()

        channels
                .zipWithNext()
                .forEach { (left, right) ->
                    launch {
                        val msg = left.receive()
                        right.send(msg.inc())
                    }
                }

        first.send(0)

        println("received message at end ${last.receive()}")
    }
}

private fun crashingThreadExample() {
    // this will take long and crash pretty early
    (0..100_000).forEach { Thread { Thread.sleep(10000) }.start() }
}
