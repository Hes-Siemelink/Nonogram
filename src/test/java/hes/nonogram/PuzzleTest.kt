package hes.nonogram

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError

class PuzzleTest {

    @Test
    fun `solve simple puzzle recursively`() {
        val puzzle = nonogram {
            row(1)
            row()

            column(1)
            column()
        }

        val solution = testSolveRecursively(puzzle)

        assertEquals("*.", solution.rows[0].cells.asString().replace('-', '.'))
        assertEquals("..", solution.rows[1].cells.asString().replace('-', '.'))
    }

    @Test
    fun `solve 3x3 puzzle recursively`() {
        val puzzle = nonogram {
            row(1)
            row(1, 1)
            row(1)

            column(1)
            column(1, 1)
            column(1)
        }

        testSolveRecursively(puzzle)
    }

    @Test
    fun `solve 4x4 puzzle recursively`() {
        val puzzle = nonogram {
            row(1)
            row(3)
            row(3)
            row(1, 1)

            column(1)
            column(3)
            column(2)
            column(3)
        }

        testSolveRecursively(puzzle)
    }

    @Test
    fun `solve 5x5 puzzle recursively`() {
        val puzzle = nonogram {
            row(3)
            row(1, 1, 1)
            row(3)
            row(1, 1)
            row(1, 1)

            column(1, 1)
            column(1, 2)
            column(3)
            column(1, 2)
            column(1, 1)
        }

        testSolveRecursively(puzzle)
    }

    @Test
    fun `solve 20x10 puzzle with logic`() {
        val puzzle = nonogram {
            row(3, 3, 2)
            row(5, 3, 2)
            row(2, 2, 2, 2)
            row(2, 2, 2, 2)
            row(3, 2, 2, 1, 2)

            row(7, 3, 3, 3)
            row(7, 10)
            row(3, 3, 9)
            row(2, 2, 3, 3)
            row(2, 3, 2, 1)

            column(3)
            column(6)
            column(7)
            column(7)
            column(2, 2)

            column(3, 2)
            column(7)
            column(1, 7)
            column(2, 3)
            column(6, 1)

            column(7)
            column(5)
            column(4)
            column(4)
            column(4)

            column(4)
            column(4)
            column(4)
            column(8)
            column(7)
        }

        testSolveWithLogic(puzzle)
    }

    @Test
    fun `solve 15x15 puzzle with logic`() {
        // From  https://activityworkshop.net/puzzlesgames/nonograms/puzzle1.html
        val puzzle = nonogram {
            row(2, 1)
            row(2, 2)
            row(4)
            row(3)
            row(5)

            row(7, 2)
            row(2, 8, 1)
            row(4, 3, 1)
            row(3, 2, 1)
            row(3, 1)

            row(3, 1)
            row(4)
            row(3, 1)
            row(3, 3)
            row(2, 4)

            column(2)
            column(3)
            column(3)
            column(5)
            column(2, 3)

            column(2, 3)
            column(1, 2, 3)
            column(1, 10)
            column(1, 5, 2)
            column(7)

            column(5)
            column(4, 1)
            column(3, 2, 2)
            column(1, 3)
            column(3, 2)
        }

        testSolveWithLogic(puzzle)
    }

    @Test
    fun `solve 30x30 puzzle with logic`() {
        val puzzle = nonogram {
            row(11, 1, 8)
            row(3, 8, 1, 8)
            row(1, 10, 1, 1, 2, 3)
            row(2, 2, 5, 1, 1, 2, 3)
            row(1, 1, 3, 4, 11)

            row(1, 1, 3, 1, 1)
            row(1, 5, 10)
            row(3, 3, 2, 1, 1, 2, 3)
            row(5, 2, 1, 2, 1, 1, 1, 2, 3)
            row(8, 2, 1, 1, 1, 2, 3)

            row(4, 2, 2, 4, 1, 8)
            row(3, 1, 2, 2, 1, 8)
            row(2, 1, 1, 2, 1, 1, 2, 3)
            row(1, 2, 2, 1, 1, 2, 3)
            row(8, 8, 1, 2, 3)

            row(2, 1, 1, 1, 2, 3)
            row(9, 1, 8, 1, 2, 3)
            row(1, 2, 4, 4, 2, 3, 1, 2, 3)
            row(1, 2, 4, 1, 2, 2, 1, 2, 3)
            row(1, 2, 2, 2, 3, 4, 2, 3)

            row(1, 2, 2, 6, 4, 2, 2, 3)
            row(1, 2, 2, 6, 4, 1, 2, 3)
            row(1, 2, 2, 6, 2, 1, 2, 3)
            row(15, 2, 2, 6)
            row(3, 2, 1)

            row(2, 1, 1)
            row(13, 1, 12)
            row(1, 2, 2, 2, 1, 2, 3, 2, 3)
            row(1, 2, 2, 2, 1, 1, 2, 1, 2)
            row(1, 2, 2, 7, 4, 3)

            column(2, 10, 8, 4)
            column(3, 6, 1, 1, 1, 1)
            column(2, 5, 1, 8, 4)
            column(1, 3, 3, 1, 8, 4)
            column(4, 2, 1, 1, 1, 1)

            column(3, 2, 1, 8, 4)
            column(5, 6, 8, 4)
            column(4, 2, 6, 1, 1)
            column(3, 1, 9, 4)
            column(3, 2, 5, 4)

            column(5, 2, 2, 4, 1, 1)
            column(4, 2, 3, 2, 5, 4)
            column(2, 2, 7, 1, 1)
            column(1, 6, 11, 3)
            column(4, 7, 1, 1, 3, 1)

            column(4, 3, 1, 3, 1, 2)
            column(1, 5, 1, 3, 2, 1)
            column(2, 1, 1, 2, 2, 1)
            column(1, 2, 5, 2)
            column(2, 1, 2, 2, 1, 3)

            column(5, 12, 1, 1, 1)
            column(1, 1, 2, 2, 1)
            column(5, 13, 4, 1, 1)
            column(2, 1, 1, 2, 1, 2)
            column(5, 18, 4)

            column(5, 18, 2, 1)
            column(2, 1, 1, 2, 1, 1, 1)
            column(5, 18, 2, 1)
            column(5, 18, 4)
            column(5, 24)
        }

        testSolveWithLogic(puzzle)
    }
}

private fun testSolveRecursively(puzzle: Puzzle): Puzzle {
    val solution = puzzle.solveRecursively() ?: throw AssertionFailedError("Puzzle should have a solution")

    solution.print()

    assertTrue(solution.isValid(), "Puzzle should be in valid state.")
    assertTrue(solution.isSolved(), "Puzzle should be solved.")

    return solution
}

private fun testSolveWithLogic(puzzle: Puzzle): Puzzle {
    val solution = puzzle.solveWithLogic(System.out) ?: throw AssertionFailedError("Puzzle should have a solution")

    solution.print()

    assertTrue(solution.isValid(), "Puzzle should be in valid state.")
    assertTrue(solution.isSolved(), "Puzzle should be solved.")

    return solution
}
