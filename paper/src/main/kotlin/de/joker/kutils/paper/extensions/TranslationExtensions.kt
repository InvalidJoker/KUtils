package de.joker.kutils.paper.extensions

val String.minecraftTranslated
    get() = "<lang:$this>"

fun String.toMinecraftTranslated(vararg args: Any): String {
    return "<lang:$this${
        if (args.isNotEmpty()) {
            args.joinToString(separator = ":", prefix = ":")
        } else {
            ""
        }
    }>"
}