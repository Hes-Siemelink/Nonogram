package hes.nonogram

import hes.nonogram.State.EMPTY
import hes.nonogram.State.UNKNOWN
import io.github.oshai.kotlinlogging.KotlinLogging

class LogicSolver() : PuzzleSolver {

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

        (puzzle.rows + puzzle.columns)
            .sortedBy { it.score() }
            .forEach {
                it.applyLogic()
            }

        log.info { "Applied logic on rows and columns:\n$puzzle\n" }
    }
}

//
// Lines
//

fun Line.score(): Int {
    return cells.size - hints.sum() - hints.size + 1
}

fun Line.checkSolved(): Boolean {
    if (this.isSolved()) {
        cells.forEach {
            if (it.state == UNKNOWN) {
                it.state = EMPTY
            }
        }
        return true
    }
    return false
}

fun Line.applyLogic() {

    if (checkSolved()) {
        return
    }

    // Get a partial solution by selecting all the positions that have the same state across all solutions
    // that are possible given the current configuration.
    val solution = possibleSolutions(this).reduce { result, next ->
        result.zip(next) { current, newState ->
            if (current == newState) current else UNKNOWN
        }
    }

    // Apply the solution to the cells of this line
    this.cells.zip(solution) { cell, state ->
        cell.state = state
    }
}


fun possibleSolutions(line: Line): List<LineState> {
    val all = allSolutions(line.hints, line.cells.size)

    return all.filter { line.isSolvedBy(it) }
}

fun allSolutions(hints: Hints, length: Int): List<LineState> {
    return solutionsCache.getOrPut(Pair(hints, length)) {
        generateAllSolutions(hints, length)
    }
}

val solutionsCache = mutableMapOf<Pair<Hints, Int>, List<LineState>>()

fun generateAllSolutions(hints: Hints, length: Int): List<LineState> {

    val hint = hints.head
    val restOfHints = hints.tail

    val all = mutableListOf<LineState>()
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
