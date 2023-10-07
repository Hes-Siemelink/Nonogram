package hes.nonogram

/**
 * Nonogram puzzle state.
 *
 * Cells are mutable; hints are read-only.
 */
data class Puzzle(
    private val rowHints: List<Hints>,
    private val columnHints: List<Hints>,
    private val cells: List<Cell> = mutableListOf()
) {

    private val width: Int = columnHints.size
    private val height: Int = rowHints.size

    val rows: List<Line>
    val columns: List<Line>

    init {
        // Initialize with empty cells if we are not copying
        if (cells.isEmpty() && cells is MutableList) {
            for (i in 1..width * height) {
                cells.add(Cell())
            }
        }

        rows = rowHints.mapIndexed { index, hints -> Line(row(index), hints) }
        columns = columnHints.mapIndexed { index, hints -> Line(column(index), hints) }
    }

    private fun row(index: Int): List<Cell> {
        return cells.subList(index * width, (index + 1) * width)
    }

    private fun column(index: Int): List<Cell> {
        val cellColumn = mutableListOf<Cell>()
        for (row in 0 until height) {
            cellColumn.add(cells[width * row + index])
        }
        return cellColumn
    }

    fun cell(row: Int, column: Int): Cell {
        return rows[row].cells[column]
    }

    fun isSolved(): Boolean = rows.all { it.isSolved() } && columns.all { it.isSolved() }

    fun copy(): Puzzle {
        val cellsCopy = cells.map { it.copy() }

        return Puzzle(rowHints, columnHints, cellsCopy)
    }

    override fun toString(): String {
        return buildString {
            columns.forEach {
                append(it.hints)
                append(" ")
            }
            append("\n")
            rows.forEach {
                for (char in it.toString().toCharArray()) {
                    append(char)
                    append(' ')
                }
                append(" ")
                append(it.hints)
                append('\n')
            }
        }
    }

    fun print() {
        rows.forEach {
            println(it.cells.asString().replace(".", "  ").replace("-", "· ").replace("*", "██"))
        }
    }
}

typealias Hints = List<Int>
typealias LineState = List<State>

class PuzzleSpec(
    val rowHints: MutableList<Hints> = mutableListOf(),
    val columnHints: MutableList<Hints> = mutableListOf()
) {
    fun row(vararg hints: Int) {
        rowHints.add(hints.asList())
    }

    fun column(vararg hints: Int) {
        columnHints.add(hints.asList())
    }

    fun toPuzzle(rowHints: List<Hints>, columnHints: List<Hints>): Puzzle {
        return Puzzle(rowHints, columnHints)
    }
}

fun nonogram(init: PuzzleSpec.() -> Unit): Puzzle {
    val spec = PuzzleSpec()
    spec.init()
    return spec.toPuzzle(spec.rowHints, spec.columnHints)
}

fun interface PuzzleSolver {
    fun solve(puzzle: Puzzle): Puzzle?
}
