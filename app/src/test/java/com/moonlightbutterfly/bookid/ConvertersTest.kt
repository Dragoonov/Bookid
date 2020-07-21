package com.moonlightbutterfly.bookid

import com.google.gson.Gson
import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

@RunWith(Enclosed::class)
class ConvertersTest {

    class BookListTest {

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

        @Test
        fun `test string to book list`() {
            //Given
            val string = generateBookListString(3)
            //When
            val testList = Converters.fromStringToBookList(string)
            //Then
            assertThat(testList, hasSize(3))
        }

        @Test
        fun `test string to empty book list`() {
            //Given
            val string = generateBookListString(0)
            //When
            val testList = Converters.fromStringToBookList(string)
            //Then
            assertThat(testList, hasSize(0))
        }

        @Test
        fun `test empty string to book list`() {
            //Given
            val string = ""
            //When
            val testList = Converters.fromStringToBookList(string)
            //Then
            assertThat(testList, nullValue())
        }

        @Test
        fun `test invalid string to book list`() {
            //Given
            val testList = "invalid"
            //When
            val result = Converters.fromStringToBookList(testList)
            //Then
            assertThat(result, nullValue())
        }

        @Test
        fun `test book list to string to book list`() {
            //Given
            val initialList = generateBookList(5)
            //When
            val string = Converters.fromBookListToString(initialList)
            val testList = Converters.fromStringToBookList(string!!)
            //Then
            assertThat(testList, allOf(hasSize(5), instanceOf(List::class.java)))
        }

    }

    class AuthorTest {

        private fun generateAuthorString(): String = Gson().toJson(Author(1))

        @Test
        fun `test string to author`() {
            //Given
            val string = generateAuthorString()
            //When
            val testAuthor = Converters.fromStringToAuthor(string)
            //Then
            assertThat(testAuthor, (allOf(instanceOf(Author::class.java), hasProperty("id"))))
        }

        @Test
        fun `test empty string to author`() {
            //Given
            val string = ""
            //When
            val testAuthor = Converters.fromStringToAuthor(string)
            //Then
            assertThat(testAuthor, nullValue())
        }

        @Test
        fun `test invalid string to author`() {
            //Given
            val string = "invalid"
            //When
            val testAuthor = Converters.fromStringToAuthor(string)
            //Then
            assertThat(testAuthor, nullValue())
        }

        @Test
        fun `test author to string to author`() {
            //Given
            val initialAuthor = Author(2)
            //When
            val string = Converters.fromAuthorToString(initialAuthor)
            val testAuthor = Converters.fromStringToAuthor(string!!)
            //Then
            assertThat(
                testAuthor,
                allOf(hasProperty("id", equalTo(2)), instanceOf(Author::class.java))
            )
        }
    }

    class ShelfListTest {

        private fun generateShelfList(size: Int): List<Shelf> = ArrayList<Shelf>().apply {
            for (x in 0 until size) {
                add(Shelf(x.toString(), ArrayList(), "", x))
            }
        }

        private fun generateShelfListString(size: Int): String = Gson().toJson(ArrayList<Shelf>().apply {
            for (x in 0 until size) {
                add(Shelf(x.toString(), ArrayList(), "", x))
            }
        })

        @Test
        fun `test string to shelf list`() {
            //Given
            val string = generateShelfListString(5)
            //When
            val testShelfList = Converters.fromStringToShelfList(string)
            //Then
            assertThat(testShelfList, hasSize(5))
        }

        @Test
        fun `test string to empty shelf list`() {
            //Given
            val string = generateShelfListString(0)
            //When
            val testShelfList = Converters.fromStringToShelfList(string)
            //Then
            assertThat(testShelfList, hasSize(0))
        }

        @Test
        fun `test empty string to shelf list`() {
            //Given
            val string = ""
            //When
            val testShelfList = Converters.fromStringToShelfList(string)
            //Then
            assertThat(testShelfList, nullValue())
        }

        @Test
        fun `test invalid string to shelf list`() {
            //Given
            val string = "invalid"
            //When
            val testShelfList = Converters.fromStringToShelfList(string)
            //Then
            assertThat(testShelfList, nullValue())
        }

        @Test
        fun `test shelf list to string to shelf list`() {
            //Given
            val initialShelfList = generateShelfList(3)
            //When
            val string = Converters.fromShelfListToString(initialShelfList)
            val testShelfList = Converters.fromStringToShelfList(string!!)
            //Then
            assertThat(testShelfList, allOf(hasSize(3), instanceOf(List::class.java)))
        }
    }

    class ObjectTest {

        private fun generateBook() = Book(1, "title")

        private fun generateShelf() = Shelf("name", ArrayList(), "id", 1)

        @Test
        fun `test book to string to book`() {
            //Given
            val book = generateBook()
            //When
            val bookString = Converters.convertToJSONString(book)
            val testBook = Converters.convertToObject<Book>(bookString)
            //Then
            assertThat(testBook, equalTo(book))
        }

        @Test
        fun `test shelf to string to shelf`() {
            //Given
            val shelf = generateShelf()
            //When
            val shelfString = Converters.convertToJSONString(shelf)
            val testShelf = Converters.convertToObject<Shelf>(shelfString)
            //Then
            assertThat(testShelf, equalTo(shelf))
        }

    }
}