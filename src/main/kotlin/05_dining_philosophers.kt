import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.selectUnbiased
import kotlin.random.Random.Default.nextInt

const val eat_duration = 1000L
fun thinking() = nextInt(0, 1500).toLong()
const val timeout = 500L

fun getLeft(number: Int): Int = ((number + 5 + 1) % 5)
fun getRight(number: Int): Int = ((number + 5 - 1) % 5)

fun main() {
    runBlocking {
        val stats = mutableListOf(0, 0, 0, 0, 0)
        val forks = (0..4).map { Channel<Int>() }
        //init forks
        forks.forEachIndexed { index, channel ->
            launch { channel.send(index) }
        }

        repeat(5) { philosopher ->
            launch {
                while (true) {
                    delay(thinking())

                    val firstFork = selectUnbiased<Int> {
                        forks[getLeft(philosopher)].onReceive { it }
                        forks[getRight(philosopher)].onReceive { it }
                    }

                    selectUnbiased<Unit> {
                        val eat: suspend (Int) -> Unit = {
                            stats[philosopher] = stats[philosopher] + 1
                            println(stats)

                            delay(eat_duration)
                            forks[getLeft(philosopher)].send(getLeft(philosopher))
                            forks[getRight(philosopher)].send(getRight(philosopher))
                        }
                        forks[getLeft(philosopher)].onReceive(eat)
                        forks[getRight(philosopher)].onReceive(eat)
                        onTimeout(timeout) {
                            println("$philosopher waited to long for second fork")
                            forks[firstFork].send(firstFork)// return first fork
                        }
                    }
                }
            }
        }
    }
}
