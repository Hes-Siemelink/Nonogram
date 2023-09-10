package hes.nonogram

import hes.nonogram.Cell.Companion.from
import hes.nonogram.State.*

class Line(val cells: List<Cell>, val hints: List<Int>) {

    constructor(cells: String, vararg hints: Int) : this(from(cells), hints.toList())

    override fun toString(): String {
        return cells.asString()
    }

    fun copy(): Line {
        return Line(cells.map { it.copy() }, hints)
    }

    fun isSolved(): Boolean {
        val counted = mutableListOf<Int>()
        var count = 0
        for (cell in cells) {
            if (cell.state == FILLED) {
                count++
            } else {
                if (count > 0) {
                    counted.add(count)
                }
                count = 0
            }
        }
        if (count > 0) {
            counted.add(count)
        }

        return counted == hints
    }

    fun possibleSolutions(): List<List<State>> {
        // TODO make more efficient byt combining filtering in 'allSolutions'.
        val all = allSolutions(hints, cells.size)

        return all.filter { this.isSolvedBy(it) }
    }

    private fun isSolvedBy(solution: List<State>): Boolean {
        return cells.zip(solution).all { (current, solution) -> solution.solves(current.state) }
    }
}

//
// Generate possibilities
//

fun allSolutions(hints: List<Int>, size: Int): List<List<State>> {

    val hint = hints.head
    val tail = hints.tail

    val all = mutableListOf<List<State>>()
    for (i in 0..size - hint) {
        val begin = EMPTY.times(i) + FILLED.times(hint)

        if (tail.isEmpty()) {
            all.add(begin + EMPTY.times(size - begin.size))
        } else {
            val beginPlus = begin + listOf(EMPTY)
            val rest = allSolutions(tail, size - beginPlus.size)
            rest.forEach {
                all.add(beginPlus + it)
            }
        }
    }

    return all
}

fun Line.applyLogic(): Line {

    // Get a partial solution by selecting all the positions that have the same state across all solutions
    // that are possible given the current configuration.
    val solution = possibleSolutions().reduce { result, next ->
        result.zip(next) { current, newState ->
            if (current == newState) current else UNKNOWN
        }
    }

    // Apply the solution to the cells of this line
    cells.zip(solution) { cell, state ->
        cell.state = state
    }

    return this
}

//
// Util
//

private val <T> List<T>.head: T
    get() = first()

private val <T> List<T>.tail: List<T>
    get() = subList(1, size)

