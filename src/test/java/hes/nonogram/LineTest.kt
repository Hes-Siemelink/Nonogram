package hes.nonogram

import hes.nonogram.State.FILLED
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LineTest {

    @Test
    fun `string representation`() {
        val cells = Cell.from(".-*")

        assertEquals(3, cells.size)
        assertEquals(State.UNKNOWN, cells[0].state)
        assertEquals(State.EMPTY, cells[1].state)
        assertEquals(FILLED, cells[2].state)

        assertEquals(".-*", cells.asString())
    }

    @Test
    fun `segment validation with hint of size 1`() {
        assertTrue(LineSegment(".", 1).isValid())
        assertTrue(LineSegment("..", 1).isValid())
        assertTrue(LineSegment("*.", 1).isValid())
        assertTrue(LineSegment(".*", 1).isValid())
        assertTrue(LineSegment("*-", 1).isValid())
        assertTrue(LineSegment("-*", 1).isValid())
        assertTrue(LineSegment("--*..", 1).isValid())

        assertFalse(LineSegment("**", 1).isValid())
        assertFalse(LineSegment("--", 1).isValid())
    }

    @Test
    fun `segment validation with hint of size 2`() {
        assertTrue(LineSegment("..", 2).isValid())
        assertTrue(LineSegment("*.", 2).isValid())
        assertTrue(LineSegment(".*", 2).isValid())
        assertTrue(LineSegment("--*..", 2).isValid())
        assertTrue(LineSegment("**", 2).isValid())

        assertFalse(LineSegment("*-", 2).isValid())
        assertFalse(LineSegment("-*", 2).isValid())
        assertFalse(LineSegment("--", 2).isValid())
    }

    @Test
    fun `line validation for 1 hint`() {
        assertTrue(Line("-", 0).isValid())
        assertTrue(Line(".", 1).isValid())
        assertTrue(Line("..", 1).isValid())
        assertTrue(Line("*.", 2).isValid())
        assertTrue(Line("-***", 3).isValid())
        assertTrue(Line("***--", 3).isValid())
        assertTrue(Line("-*...", 3).isValid())
    }

    @Test
    fun `line validation for 2 hints`() {
        assertTrue(Line("...", 1, 1).isValid())
        assertTrue(Line("*.*", 1, 1).isValid())
        assertTrue(Line("..*.*", 1, 3).isValid())
        assertTrue(Line("-*..", 1, 1).isValid())
        assertTrue(Line("*-*..", 1, 2).isValid())
        assertTrue(Line("*-*.*", 1, 1, 1).isValid())
        assertTrue(Line("..****..", 4, 1).isValid())
        assertTrue(Line("..****..", 1, 4).isValid())
        assertTrue(Line("*-*..", 1, 1).isValid())

        assertFalse(Line(".*.", 1, 1).isValid())
        assertFalse(Line(".*.*.", 1, 1, 1).isValid())
        assertFalse(Line("---*", 1, 1).isValid())
        assertFalse(Line(".***..", 2, 1).isValid())
        assertFalse(Line("****", 2, 2).isValid())
    }

    @Test
    fun `line validation for 3 hints`() {
        assertTrue(Line(".....", 1, 1, 1).isValid())

        assertFalse(Line("**....-.", 2, 2, 2).isValid())
    }

    @Test
    fun `line solved`() {
        assertTrue(Line("*", 1).isSolved())
        assertTrue(Line("*-**.", 1, 2).isSolved())
        assertTrue(Line("-", 0).isSolved())

        assertFalse(Line("*-.*.", 1, 2).isSolved())
        assertFalse(Line("*-***", 1, 2).isSolved())
        assertFalse(Line("-------.****.--", 5).isSolved())
    }

    @Test
    fun `copy line`() {
        val line = Line("...", 1)

        val copy = line.copy()
        copy.cells[0].state = FILLED

        assertEquals("*..", copy.cells.asString())
        assertEquals("...", line.cells.asString())
    }

    @Test
    fun `generate possibilities for (1) length 5`() {
        val result = allSolutions(listOf(1), 5)
            .map { it.asString() }

        result.forEach {
            println(it)
        }

        assertThat(result).containsExactlyInAnyOrderElementsOf(
            listOf(
                "*----",
                "-*---",
                "--*--",
                "---*-",
                "----*"
            )
        )
    }

    @Test
    fun `generate possibilities for (1,1) length 5`() {
        val result = allSolutions(listOf(1, 1), 5)
            .map { it.asString() }

        assertThat(result).containsExactlyInAnyOrderElementsOf(
            listOf(
                "*-*--",
                "*--*-",
                "*---*",
                "-*-*-",
                "-*--*",
                "--*-*"
            )
        )
    }

    @Test
    fun `generate possibilities for (1,1) length 5 given a partial solution`() {

        val line = Line("*....", 1, 1)

        val result = possibleSolutions(line)
            .map { it.asString() }

        assertThat(result).containsExactlyInAnyOrderElementsOf(
            listOf(
                "*-*--",
                "*--*-",
                "*---*",
            )
        )
    }

    @Test
    fun `Possibilities for (1,3) with length 5 should have one solution`() {

        val line = Line(".....", 1, 3)

        val result = possibleSolutions(line)
            .map { it.asString() }

        assertThat(result).hasSize(1)
        assertThat(result).containsExactlyInAnyOrderElementsOf(
            listOf(
                "*-***"
            )
        )
    }

    @Test
    fun `Apply logic to get partial solution`() {

        testApplyLogic(Line("...", 2), ".*.")
        testApplyLogic(Line("-*.", 2), "-**")
        testApplyLogic(Line("..-", 2), "**-")
        testApplyLogic(Line("..-.-", 2), "**---")
    }

    private fun testApplyLogic(line: Line, result: String) {
        line.applyLogic()

        assertThat(line.cells.asString()).isEqualTo(result)
    }
}