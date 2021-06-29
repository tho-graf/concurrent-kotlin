import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select
import kotlin.random.Random.Default.nextInt

fun main() {
    runBlocking {
        val serverCh1 = Channel<Int>()
                .also {
                    //fake async server call, which takes between 500 and 1000ms
                    launch {
                        delay(nextInt(500, 1000).toLong())
                        it.send(42)
                    }
                }

        val serverCh2 = Channel<Int>()
                .also {
                    //fake async server call, which takes between 500 and 1000ms
                    launch {
                        delay(nextInt(500, 1000).toLong())
                        it.send(42)
                    }
                }

        select<Unit> {
            onTimeout(700) { println("timeout, no response after 700ms") }
            serverCh1.onReceive { println("Answer from server 1 $it") }
            serverCh2.onReceive { println("Answer from server 2 $it") }
        }
    }
}

