package com.moonlightbutterfly.bookid

import com.google.gson.Gson
import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Test

class ConvertersTest {

    private fun generateBookList(size: Int): List<Book> = ArrayList<Book>().apply {
        for (x in 0 until size) {
            add(Book(x))
        }
    }

    private fun generateBookListString(size: Int): String = Gson().toJson(ArrayList<Book>().apply {
        for (x in 0 until size) {
            add(Book(x))
        }
    })

    private fun generateAuthorString(): String = Gson().toJson(Author(1))

    @Test
    fun `test string to book list`() {
        val testList = Converters.fromStringToBookList(generateBookListString(3))
        assertThat(testList, hasSize(3))
    }

    @Test
    fun `test string to empty book list`() {
        val testList = Converters.fromStringToBookList(generateBookListString(0))
        assertThat(testList, hasSize(0))
    }

    @Test
    fun `test empty string to book list`() {
        val testList = Converters.fromStringToBookList("")
        assertThat(testList, nullValue())
    }

    @Test
    fun `test invalid string to book list`() {
        val testList = "invalid"
        assertThat(Converters.fromStringToBookList(testList), nullValue())
    }

    @Test
    fun `test book list to string to book list`() {
        val initialList = generateBookList(5)
        val string = Converters.fromBookListToString(initialList)
        val testList = Converters.fromStringToBookList(string!!)
        assertThat(testList, allOf(hasSize(5), instanceOf(List::class.java)))
    }

    @Test
    fun `test string to author`() {
        val testAuthor = Converters.fromStringToAuthor(generateAuthorString())
        assertThat(testAuthor, (allOf(instanceOf(Author::class.java), hasProperty("id"))))
    }

    @Test
    fun `test empty string to author`() {
        val testAuthor = Converters.fromStringToAuthor("")
        assertThat(testAuthor, nullValue())
    }

    @Test
    fun `test invalid string to author`() {
        val testAuthor = Converters.fromStringToAuthor("invalid")
        assertThat(testAuthor, nullValue())
    }

    @Test
    fun `test author to string to author`() {
        val initialAuthor = Author(2)
        val string = Converters.fromAuthorToString(initialAuthor)
        val testAuthor = Converters.fromStringToAuthor(string!!)
        assertThat(testAuthor, allOf(hasProperty("id", equalTo(2)), instanceOf(Author::class.java)))
    }

}