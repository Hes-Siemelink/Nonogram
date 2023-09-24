package hes.logic

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LogicTest {

    // parent(Emma, Valeria)
    // parent(Emma, Hes)
    // parent(Mateo, Valeria)
    // parent(Mateo, Hes)
    // parent(Sam, Valeria)
    // parent(Sam, Hes)
    // parent(X, Hes) :- parent(X, Valeria)
    // parent(X, Valeria) :- parent(X, Hes)

    @Test
    fun findParents() {

        // Given
        val data = Data()
        val X = Var("X")
        data.addFact(Term("parent", "Emma", "Valeria"))
        data.addRule(
            Term("parent", X, "Valeria"),
            Term("parent", X, "Hes")
        )

        val result = data.query(Term("parent", "Emma", X))
        assertThat(result).contains(Term("parent", "Emma", "Valeria"))
        assertThat(result).contains(Term("parent", "Emma", "Hes"))
    }
}


class Data {

    private val terms = mutableSetOf<Term>()
    private val rules = mutableMapOf<Term, List<Term>>()

    fun addFact(term: Term) {
        terms.add(term)
    }

    fun addRule(term: Term, vararg conditions: Term) {
        rules.put(term, conditions.toList())
    }
}

data class Var(val name: String, val value: Term? = null) {
    val bound: Boolean
        get() = value != null
}

data class Term(val fact: String, val arguments: List<Any>) {
    constructor(fact: String, vararg arguments: Any) : this(fact, arguments.toList())

    fun match(other: Term): Match? {
        if (this == other) {
            return Match(this)
        }

        if (arguments.size != other.arguments.size) {
            return null
        }

        val vars = mutableListOf<Var>()
        for ((index, arg) in arguments.withIndex()) {
            if (arg is Var && other.arguments[index] is Var) {
                // TODO
            } else if (arg !is Var && other.arguments[index] is Var) {
                // TODO Match variables
            }
        }

        return null
    }
}

data class Match(val term: Term, val variables: List<Var> = emptyList()) {

}

fun args(vararg arguments: Any): List<Any> {
    return listOf(arguments)
}

fun Data.query(query: Term): List<Term> {
    // FIXME implement
    return emptyList()
}
