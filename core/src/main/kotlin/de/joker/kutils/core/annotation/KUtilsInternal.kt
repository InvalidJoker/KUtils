package de.joker.kutils.core.annotation

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.TYPEALIAS, AnnotationTarget.CLASS, AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION)
@RequiresOptIn(
    message = "This is an internal API that should not be used outside of KUtils." +
            "No compatibility guarantees are provided." +
            "If there is a reason you are using an Internal API (eg. no other way is provided)" +
            "you should report this to the KUtils team.",
    level = RequiresOptIn.Level.ERROR
)
annotation class KUtilsInternal