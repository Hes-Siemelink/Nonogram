package hes.nonogram

import hes.nonogram.Cell.Companion.from
import hes.nonogram.State.FILLED

class Line(val cells: List<Cell>, val hints: Hints) {

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

        return counted == hints || (counted.isEmpty() && hints == listOf(0))
    }

    fun isSolvedBy(solution: List<State>): Boolean {
        return cells.zip(solution).all { (current, solution) -> solution.solves(current.state) }
    }
}

