package sample

actual class Sample {
    actual fun checkMe() = 42
}

actual object Platform {
    actual val name: String = "JVM"
}

actual sealed class Do<T> {
    actual class Map<T, R> actual constructor(val d: Do<T>, val f: (T) -> R) : Do<R>() {
        override fun run(): R = f(d.run())
    }

    actual class Pure<R> actual constructor(val x: R) : Do<R>() {
        override fun run(): R = x
    }

    actual class Join<T1, T2, R> actual constructor(val d1: Do<T1>, val d2: Do<T2>, val f: (T1, T2) -> R) : Do<R>() {
        override fun run(): R = f(d1.run(), d2.run()) // TODO: make concurrent
    }

    actual class Then<T, R> actual constructor(val d: Do<T>, val f: (T) -> Do<R>) : Do<R>() {
        override fun run(): R = f(d.run()).run()
    }

    actual class Print actual constructor(val x: String) : Do<Unit>() {
        override fun run() = println(x)
    }

    actual object End : Do<Nothing>() {
        override fun run(): Nothing {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    actual fun <R> map(f: (T) -> R): Map<T, R> = Map(this, f)
    actual fun <T2, R> join(d: Do<T2>, f: (T, T2) -> R): Join<T, T2, R> = Join(this, d, f)
    actual fun <R> then(f: (T) -> Do<R>): Then<T, R> = Then(this, f)
    actual abstract fun run(): T
}
