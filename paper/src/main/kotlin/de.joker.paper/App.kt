package de.joker.paper

import de.joker.core.Printer

fun main() {
    val name = "Kotlin"
    val message = "Hello, $name!"
    val printer = Printer(message)
    printer.printMessage()

    for (i in 1..5) {
        println("i = $i")
    }
}
