package hes.nonogram

fun <T : Any> unwrapCompanionClass(ofClass: Class<T>): Class<*> {
    return ofClass.enclosingClass?.takeIf {
        ofClass.enclosingClass.kotlin.objectInstance?.javaClass == ofClass
    } ?: ofClass
}

val <T> List<T>.head: T
    get() = first()

val <T> List<T>.tail: List<T>
    get() = subList(1, size)

