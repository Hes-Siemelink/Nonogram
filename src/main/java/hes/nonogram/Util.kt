package hes.nonogram

import java.util.logging.Logger

fun <R : Any> R.logger(): Lazy<Logger> {
    return lazy { Logger.getLogger(unwrapCompanionClass(this.javaClass).name) }
}

fun <T : Any> unwrapCompanionClass(ofClass: Class<T>): Class<*> {
    return ofClass.enclosingClass?.takeIf {
        ofClass.enclosingClass.kotlin.objectInstance?.javaClass == ofClass
    } ?: ofClass
}

val <T> List<T>.head: T
    get() = first()

val <T> List<T>.tail: List<T>
    get() = subList(1, size)

