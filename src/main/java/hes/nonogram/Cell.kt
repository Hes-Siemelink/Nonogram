package hes.nonogram

data class Cell(var state: State = State.UNKNOWN) {

    constructor(char: Char) : this(State.from(char))

    fun copy(): Cell {
        return Cell(state)
    }

    override fun toString(): String {
        return state.toString()
    }

    companion object {
        fun from(s: String): List<Cell> {
            val cells = mutableListOf<Cell>()
            for (char: Char in s) {
                if (char.isWhitespace()) {
                    continue
                }
                cells.add(Cell(char))
            }
            return cells
        }
    }

}

enum class State(private val value: Char) {

    UNKNOWN('.'), EMPTY('-'), FILLED('*');

    fun solves(other: State): Boolean {
        return this == UNKNOWN || other == UNKNOWN || this == other
    }

    override fun toString(): String {
        return value.toString()
    }

    fun times(size: Int): LineState {
        return (1..size).map { this }
    }

    companion object {
        fun from(char: Char): State {
            State.values().forEach {
                if (it.value == char) {
                    return it
                }
            }
            throw IllegalArgumentException("The character '$char' does not have a state.")
        }
    }
}

fun List<Any>.asString(): String {
    return buildString {
        this@asString.forEach { append(it) }
    }
}

