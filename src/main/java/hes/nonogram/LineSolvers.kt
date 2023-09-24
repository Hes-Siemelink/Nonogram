package hes.nonogram

interface LineSolver {
    fun applyLogic(line: Line)
}

class BasicSolver : LineSolver {
    override fun applyLogic(line: Line) {
        // Get a partial solution by selecting all the positions that have the same state across all solutions
        // that are possible given the current configuration.
        val solution = possibleSolutions(line).reduce { result, next ->
            result.zip(next) { current, newState ->
                if (current == newState) current else State.UNKNOWN
            }
        }

        // Apply the solution to the cells of this line
        line.cells.zip(solution) { cell, state ->
            cell.state = state
        }
    }
}

fun possibleSolutions(line: Line): List<List<State>> {
    // TODO make more efficient byt combining filtering in 'allSolutions'.
    val all = allSolutions(line.hints, line.cells.size)

    return all.filter { line.isSolvedBy(it) }
}

fun allSolutions(hints: List<Int>, size: Int): List<List<State>> {

    val hint = hints.head
    val tail = hints.tail

    val all = mutableListOf<List<State>>()
    for (i in 0..size - hint) {
        val begin = State.EMPTY.times(i) + State.FILLED.times(hint)

        if (tail.isEmpty()) {
            all.add(begin + State.EMPTY.times(size - begin.size))
        } else {
            val beginPlus = begin + listOf(State.EMPTY)
            val rest = allSolutions(tail, size - beginPlus.size)
            rest.forEach {
                all.add(beginPlus + it)
            }
        }
    }

    return all
}
