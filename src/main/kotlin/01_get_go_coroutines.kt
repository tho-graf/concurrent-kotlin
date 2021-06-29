import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CompletableFuture

fun main() {
    val result1 = syncHelloWorld()
    println("result1: $result1")

    val asyncHelloWorld: CompletableFuture<String> = asyncHelloWorld()
    asyncHelloWorld.thenAccept { result2 -> println("result2: $result2") }

    runBlocking {
        val result3 = suspendingAsyncHelloWorld()
        println("result3: $result3")
    }
}


fun syncHelloWorld(): String = "Hello World"

fun asyncHelloWorld(): CompletableFuture<String> = CompletableFuture.supplyAsync {
    Thread.sleep(1000)
    "Hello World"
}

suspend fun suspendingAsyncHelloWorld(): String = coroutineScope {
    delay(1000)
    "Hello World"
}
