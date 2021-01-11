package berryj.tutorial.reactive.nonblocking

import berryj.tutorial.reactive.nonblocking.client.CovidThailandClient

fun main() {
//    val y = CovidThailandClient.daily().block()
//    println(y)
    val x = CovidThailandClient.timeline().doOnNext { println(it) }.blockLast()
    println(x)
}

