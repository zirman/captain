package sample

expect class Sample() {
    fun checkMe(): Int
}

expect sealed class Do<T> {
    class Map<T, R>(d: Do<T>, f: (T) -> R) : Do<R>
    class Pure<R>(x: R) : Do<R>
    class Join<T1, T2, R>(d1: Do<T1>, d2: Do<T2>, f: (T1, T2) -> R) : Do<R>
    class Then<T, R>(d: Do<T>, f: (T) -> Do<R>) : Do<R>
    class Print(x: String) : Do<Unit>
    object End : Do<Nothing>

    fun <R> map(f: (T) -> R): Map<T, R>
    fun <T2, R> join(d: Do<T2>, f: (T, T2) -> R): Join<T, T2, R>
    fun <R> then(f: (T) -> Do<R>) : Then<T, R>

    abstract fun run() : T
}

fun <R> pure(x: R) : Do.Pure<R> = Do.Pure(x)
fun print(x: String) : Do.Print = Do.Print(x)

fun dashboard(): Do<Unit> =
    pure(1)
        .join(pure(2)) { x, y -> x + y }
        .then { x -> print(x.toString()) }

expect object Platform {
    val name: String
}

fun hello(): String = "Hello from ${Platform.name}"
