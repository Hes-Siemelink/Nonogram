package hes.nonogram

import hes.nonogram.Cell.State.FILLED
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PuzzleTest {

    @Test
    fun `create new puzzle`() {
        val puzzle = Puzzle.from(
            listOf(
                listOf(1),
                emptyList()
            ),
            listOf(
                listOf(1),
                emptyList()
            )
        )

        assertEquals("..", toString(puzzle.rows[0].cells))
        assertEquals("..", toString(puzzle.rows[1].cells))
        assertEquals("..", toString(puzzle.columns[0].cells))
        assertEquals("..", toString(puzzle.columns[1].cells))

        assertTrue(puzzle.valid, "Puzzle should be in valid state.")
        assertFalse(puzzle.solved, "Puzzle should not be solved.")

        puzzle.cell(0, 0).state = FILLED

        assertTrue(puzzle.valid, "Puzzle should be in valid state.")
        assertTrue(puzzle.solved, "Puzzle should be solved.")
    }
}