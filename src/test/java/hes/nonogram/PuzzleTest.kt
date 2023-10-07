package hes.nonogram

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.opentest4j.AssertionFailedError

@ExtendWith(TestTimer::class)
class PuzzleTest {

    @ParameterizedTest
    @ValueSource(classes = [LogicSolver::class, RecursiveSolver::class])
    fun `Solve simple puzzle`(solverClass: Class<PuzzleSolver>) {
        val solver = solverClass.getDeclaredConstructor().newInstance()

        val puzzle = nonogram {
            row(1)
            row()

            column(1)
            column()
        }

        val solution = testSolve(puzzle, solver)

        assertEquals("*.", solution.rows[0].cells.asString().replace('-', '.'))
        assertEquals("..", solution.rows[1].cells.asString().replace('-', '.'))
    }

    @ParameterizedTest
    @ValueSource(classes = [LogicSolver::class, RecursiveSolver::class])
    fun `Solve 3x3 puzzle`(solverClass: Class<PuzzleSolver>) {
        val solver = solverClass.getDeclaredConstructor().newInstance()
        val puzzle = nonogram {
            row(1)
            row(1, 1)
            row(1)

            column(1)
            column(1, 1)
            column(1)
        }

        testSolve(puzzle, solver)
    }

    @Test
    fun `Solve 4x4 puzzle recursively`() {
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

        testSolve(puzzle, RecursiveSolver())

        // This puzzle has multiple solutions so logic solver won't find a solution.
    }

    @ParameterizedTest
    @ValueSource(classes = [LogicSolver::class, RecursiveSolver::class])
    fun `Solve 5x5 puzzle`(solverClass: Class<PuzzleSolver>) {
        val solver = solverClass.getDeclaredConstructor().newInstance()

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

        testSolve(puzzle, solver)
    }

    @ParameterizedTest
    @ValueSource(classes = [LogicSolver::class, RecursiveSolver::class])
    fun `Solve puzzle with hint = 0 in it`(solverClass: Class<PuzzleSolver>) {
        val solver = solverClass.getDeclaredConstructor().newInstance()
        val puzzle = nonogram {
            row(1, 1, 1)
            row(1, 1, 1)
            row(3, 1)
            row(1, 1, 1)
            row(1, 1, 1)

            column(5)
            column(1)
            column(5)
            column(0)
            column(5)
        }

        testSolve(puzzle, LogicSolver())
    }

    @Test
    fun `Solve 20x10 puzzle with logic`() {
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

        testSolve(puzzle, LogicSolver())
    }

    @Test
    fun `Solve 15x15 puzzle with logic`() {
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

        testSolve(puzzle, LogicSolver())
    }

    @Test
    fun `Solve 30x30 puzzle with logic`() {
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

        testSolve(puzzle, LogicSolver())
    }

    @Test
    fun `Puzzle #99 form Sandra`() {
        val puzzle = nonogram {
            row(3, 3, 2, 3, 3)
            row(1, 2, 4, 2, 1)
            row(1, 3, 6, 3, 1)
            row(3, 3, 3, 3)
            row(8, 8)

            row(2, 3, 2, 3, 2)
            row(1, 3, 4, 3, 1)
            row(3, 1, 2, 1, 3)
            row(3, 2, 2, 3)
            row(3, 3, 2, 3, 3)

            row(3, 2, 3)
            row(3, 3, 3, 3)
            row(3, 6, 3)
            row(1, 3, 1, 1, 3, 1)
            row(2, 3, 2, 3, 2)

            row(8, 8)
            row(3, 3, 3, 3)
            row(1, 3, 6, 3, 1)
            row(1, 2, 4, 2, 1)
            row(3, 3, 2, 3, 3)

            column(3, 3, 2, 3, 3)
            column(1, 2, 4, 2, 1)
            column(1, 3, 6, 3, 1)
            column(3, 3, 3, 3)
            column(8, 8)

            column(2, 3, 1, 3, 2)
            column(1, 3, 2, 1, 3, 1)
            column(3, 3, 2, 3)
            column(3, 1, 3, 3)
            column(3, 3, 2, 1, 1, 3)

            column(3, 3, 2, 1, 1, 3)
            column(3, 1, 3, 3)
            column(3, 3, 2, 3)
            column(1, 3, 2, 1, 3, 1)
            column(2, 3, 1, 3, 2)

            column(8, 8)
            column(3, 3, 3, 3)
            column(1, 3, 6, 3, 1)
            column(1, 2, 4, 2, 1)
            column(3, 3, 2, 3, 3)
        }

        testSolve(puzzle, LogicSolver())
    }
}

private fun testSolve(puzzle: Puzzle, solver: PuzzleSolver): Puzzle {

    val solution = solver.solve(puzzle) ?: throw AssertionFailedError("Puzzle should have a solution")
    solution.print()

    assertTrue(solution.isValid(), "Puzzle should be in valid state.")
    assertTrue(solution.isSolved(), "Puzzle should be solved.")

    return solution
}