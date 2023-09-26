package hes.nonogram

class RecursiveSolver : PuzzleSolver {

    override fun solve(puzzle: Puzzle): Puzzle? {

        if (puzzle.isSolved()) {
            return puzzle
        }

        for (r in puzzle.rows.indices) {
            for (c in puzzle.rows[r].cells.indices) {
                if (puzzle.rows[r].cells[c].state != State.UNKNOWN) continue

                val candidate = puzzle.copy()
                candidate.rows[r].cells[c].state = State.FILLED

                if (!candidate.isValid()) {
                    puzzle.rows[r].cells[c].state = State.EMPTY
                    continue
                }

                println("Checking with ($r, $c):\n$candidate\n")

                val solution = solve(candidate)
                if (solution != null) {
                    return solution
                }

                puzzle.rows[r].cells[c].state = State.EMPTY
            }
        }

        return null
    }
}

fun Puzzle.isValid(): Boolean = rows.all { it.isValid() } && columns.all { it.isValid() }

fun Line.isValid(): Boolean {

    // Special case: hint is 0 and all cells are empty
    if (hints.size == 1 && hints[0] == 0 && cells.all { it.state == State.EMPTY }) {
        return true;
    }

    // Not valid if there are more cells filled in than the total of the hints
    if (filledCount() > hints.sumOf { it }) {
        return false
    }

    // Check if the cells that are filled in from the left (or top) until the first unknown cell are
    // consistent with the first hints
    if (!knownCellsFromLeftConsistentWithHints()) {
        return false
    }

    // Check the segment that each hint can logically be in
    for (i in hints.indices) {
        val hint = hints[i]
        var left = lengthOf(hints.subList(0, i)) + emptyLeft()
        val right = lengthOf(hints.subList(i + 1, hints.size)) + emptyRight()

        // Hack to detect cases like .*. with [1, 1]
        if (left > 0 && cells[left - 1].state == State.FILLED) {
            left += 1
        }

        if (left > cells.size - right) {
            return false
        }

        try {
            val segment = LineSegment(cells.subList(left, cells.size - right), hint)

            // Check if segment where this hint should lie is valid
            if (!segment.isValid()) {
                return false
            }
        } catch (e: IllegalArgumentException) {
            println("Segment: $this with hints $hints. Left: $left; right: $right")
            throw e
        }
    }
    return true
}

private fun Line.filledCount(): Int {
    return cells.count { it.state == State.FILLED }
}

private fun Line.knownCellsFromLeftConsistentWithHints(): Boolean {

    val filled = splitFilled()
    if (filled.size <= hints.size) {
        return (filled == hints.subList(0, filled.size))
    }
    return false
}

private fun Line.splitFilled(): List<Int> {
    val filled = mutableListOf<Int>()
    var count = 0
    for (cell in cells) {
        if (cell.state == State.EMPTY) {
            if (count > 0) {
                filled.add(count)
            }
            count = 0
        }
        if (cell.state == State.FILLED) {
            count++
        }
        if (cell.state == State.UNKNOWN) {
            return filled
        }
    }
    if (count > 0) {
        filled.add(count)
    }
    return filled
}

private fun Line.emptyLeft(): Int {
    var total = 0
    for (cell in cells) {
        if (cell.state != State.EMPTY) {
            return total
        }
        total++
    }
    return total
}

private fun Line.emptyRight(): Int {
    var total = 0
    for (cell in cells.reversed()) {
        if (cell.state != State.EMPTY) {
            return total
        }
        total++
    }
    return total
}

fun lengthOf(hints: List<Int>): Int {
    var total = 0
    for (hint in hints) {
        total += hint + 1
    }
    return total
}


class LineSegment(val cells: List<Cell>, val hint: Int) {

    constructor(cells: String, hint: Int) : this(Cell.from(cells), hint)

    fun isValid(): Boolean {
        for (i in 0..cells.size - hint) {
            if (isValidAt(i)) {
                return true
            }
        }
        return false
    }

    private fun isValidAt(position: Int): Boolean {
        for (i in position until position + hint) {
            if (cells[i].state == State.EMPTY) {
                return false
            }
        }

        if (position > 0 && cells[position - 1].state == State.FILLED) {
            return false
        }

        // ..* position 1 hint 1
        if (position + hint < cells.size && cells[position + hint].state == State.FILLED) {
            return false
        }

        return true
    }

    override fun toString(): String {
        return "${cells.asString()} ($hint)"
    }
}