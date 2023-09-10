package hes.nonogram

import hes.nonogram.Cell.Companion.from
import hes.nonogram.State.*

class Line(val hints: List<Int>, val cells: List<Cell>) {

    constructor(hints: List<Int>, s: String) : this(hints, from(s))

    override fun toString(): String {
        return cells.asString()
    }

    fun copy(): Line {
        return Line(hints, cells.map { it.copy() })
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

    fun possibileSolutions(): List<List<State>> {
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

private val <T> List<T>.head: T
    get() = first()

private val <T> List<T>.tail: List<T>
    get() = subList(1, size)

