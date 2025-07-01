package de.joker.kutils.core.utils

import java.net.InetAddress

fun getLocalIP(): String {
    return InetAddress.getLocalHost().hostAddress ?: "localhost"
}

fun getLocalIPWithPort(port: Int): String {
    return "${getLocalIP()}:$port"
}