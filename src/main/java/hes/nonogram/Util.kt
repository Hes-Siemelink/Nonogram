package hes.nonogram

val <T> List<T>.head: T
    get() = first()

val <T> List<T>.tail: List<T>
    get() = subList(1, size)

