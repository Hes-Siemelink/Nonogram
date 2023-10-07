package hes.nonogram

import io.github.oshai.kotlinlogging.KotlinLogging

class LogicSolver(private val lineSolver: LineSolver = BasicSolver()) : PuzzleSolver {

    private val log = KotlinLogging.logger {}

    override fun solve(puzzle: Puzzle): Puzzle? {

        var previousState: Puzzle?

        do {
            previousState = puzzle.copy()
            applyLogic(puzzle)
        } while (!puzzle.isSolved() && puzzle != previousState)

        return if (puzzle.isSolved()) puzzle else null
    }

    private fun applyLogic(puzzle: Puzzle) {

        puzzle.rows.forEach { lineSolver.applyLogic(it) }
        log.info { "Applied logic on rows:\n$puzzle\n" }

        puzzle.columns.forEach { lineSolver.applyLogic(it) }
        log.info { "Applied logic on columns:\n$puzzle\n" }
    }
}

//
// Lines
//

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

fun allSolutions(hints: Hints, length: Int): List<List<State>> {

    val hint = hints.head
    val restOfHints = hints.tail

    val all = mutableListOf<List<State>>()
    for (i in 0..length - hint) {
        val begin = State.EMPTY.times(i) + State.FILLED.times(hint)

        if (restOfHints.isEmpty()) {
            all.add(begin + State.EMPTY.times(length - begin.size))
        } else {
            val beginPlus = begin + listOf(State.EMPTY)
            val rest = allSolutions(restOfHints, length - beginPlus.size)
            rest.forEach {
                all.add(beginPlus + it)
            }
        }
    }

    return all
}

fun Line.allSolutions(position: Int = 0): List<List<State>> {
    val all = mutableListOf<List<State>>()

    return all
}