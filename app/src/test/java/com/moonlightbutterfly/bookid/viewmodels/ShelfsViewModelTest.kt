package com.moonlightbutterfly.bookid.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.moonlightbutterfly.bookid.MainCoroutineRule
import com.moonlightbutterfly.bookid.Manager
import com.moonlightbutterfly.bookid.ManagerFake
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import com.moonlightbutterfly.bookid.repository.internalrepo.RoomRepositoryFake
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.hasSize
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class ShelfsViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ShelfsViewModel
    private lateinit var manager: Manager
    private lateinit var internalRepo: InternalRepository

    @Before
    fun setUp() {
        manager = ManagerFake().apply {
            signInUser(
                User(
                    "1",
                    "testNick",
                    "testMail",
                    ""
                )
            )
        }
        internalRepo = RoomRepositoryFake()
        viewModel = ShelfsViewModel(manager, internalRepo, Dispatchers.Main)
    }



    @Test
    fun `test insert shelf`() = coroutineRule.runBlockingTest {
        //Given
        val shelfName = "shelf1"
        //When
        viewModel.insertShelf(shelfName)
        //Then
        assertThat(
            internalRepo.getShelfByName(shelfName),
            CoreMatchers.notNullValue()
        )
    }

    @Test
    fun `test insert null shelf`() = coroutineRule.runBlockingTest {
        //Given
        val shelfName: String? = null
        //When
        viewModel.insertShelf(shelfName)
        //Then
        internalRepo.getUserShelfs("1").collect {
            assertThat(it, nullValue())
        }
    }

    @Test
    fun `test insert book to shelf`() = coroutineRule.runBlockingTest {
        //Given
        val shelf = Shelf("shelf", ArrayList(), "1", 1)
        val book = Book(1, "title")
        //When
        internalRepo.insertShelf(shelf)
        viewModel.insertBookToShelf(shelf, book)
        val testShelf = internalRepo.getShelfById(1)
        //Then
        assertThat(testShelf?.books, hasSize(1))
    }

    @Test
    fun `test insert null book to shelf`() = coroutineRule.runBlockingTest {
        //Given
        val shelf = Shelf("shelf", ArrayList(), "1", 1)
        val book = null
        //When
        internalRepo.insertShelf(shelf)
        viewModel.insertBookToShelf(shelf, book)
        val testShelf = internalRepo.getShelfById(1)
        //Then
        assertThat(testShelf?.books, hasSize(0))
    }

    @Test
    fun `test update shelf name`() = coroutineRule.runBlockingTest {
        //Given
        val shelf = Shelf("shelf", ArrayList(), "1", 1)
        val testName = "test"
        //When
        internalRepo.insertShelf(shelf)
        viewModel.updateShelfName(shelf, testName)
        val testShelf = internalRepo.getShelfById(1)
        //Then
        assertThat(testShelf?.name, `is`(testName))
    }

    @Test
    fun `test update shelf name with null`() = coroutineRule.runBlockingTest {
        //Given
        val shelf = Shelf("shelf", ArrayList(), "1", 1)
        val testName: String? = null
        //When
        internalRepo.insertShelf(shelf)
        viewModel.updateShelfName(shelf, testName)
        val testShelf = internalRepo.getShelfById(1)
        //Then
        assertThat(testShelf?.name, `is`("shelf"))
    }

    @Test
    fun `test update non existing shelf name`() = coroutineRule.runBlockingTest {
        //Given
        val shelf = Shelf("shelf", ArrayList(), "1", 1)
        val testName: String? = null
        //When
        viewModel.updateShelfName(shelf, testName)
        val testShelf = internalRepo.getShelfById(1)
        //Then
        assertThat(testShelf?.name, nullValue())
    }

    @Test
    fun `test delete book from shelf`() = coroutineRule.runBlockingTest {
        //Given
        val book = Book(1)
        val shelf = Shelf("shelf", listOf(book).toMutableList(), "1", 1)
        //When
        internalRepo.insertShelf(shelf)
        viewModel.deleteBookFromShelf(book, shelf)
        val testShelf = internalRepo.getShelfById(1)
        //Then
        assertThat(testShelf?.books, hasSize(0))
    }

    @Test
    fun `test delete book from shelf with two books`() = coroutineRule.runBlockingTest {
        //Given
        val book = Book(1)
        val book2 = Book(2)
        val shelf = Shelf("shelf", listOf(book, book2).toMutableList(), "1", 1)
        //When
        internalRepo.insertShelf(shelf)
        viewModel.deleteBookFromShelf(book, shelf)
        val testShelf = internalRepo.getShelfById(1)
        //Then
        assertThat(testShelf?.books, hasSize(1))
    }

    @Test
    fun `test delete null book from shelf`() = coroutineRule.runBlockingTest {
        //Given
        val book = Book(1)
        val book2 = Book(2)
        val shelf = Shelf("shelf", listOf(book, book2).toMutableList(), "1", 1)
        //When
        internalRepo.insertShelf(shelf)
        viewModel.deleteBookFromShelf(null, shelf)
        val testShelf = internalRepo.getShelfById(1)
        //Then
        assertThat(testShelf?.books, hasSize(2))
    }

    @Test
    fun `test delete book from non existing shelf`() = coroutineRule.runBlockingTest {
        //Given
        val book = Book(1)
        val book2 = Book(2)
        val shelf = Shelf("shelf", listOf(book, book2).toMutableList(), "1", 1)
        //When
        viewModel.deleteBookFromShelf(book, shelf)
        val testShelf = internalRepo.getShelfById(1)
        //Then
        assertThat(testShelf?.books, nullValue())
    }

    @Test
    fun `test delete shelf`() = coroutineRule.runBlockingTest {
        //Given
        val shelf = Shelf("shelf", ArrayList(), "1", 1)
        //When
        internalRepo.insertShelf(shelf)
        viewModel.deleteShelf(shelf)
        val testShelf = internalRepo.getShelfById(1)
        //Then
        assertThat(testShelf, nullValue())
    }

    @Test
    fun `test delete null shelf`() = coroutineRule.runBlockingTest {
        //Given
        val shelf = Shelf("shelf", ArrayList(), "1", 1)
        //When
        internalRepo.insertShelf(shelf)
        viewModel.deleteShelf(null)
        val testShelf = internalRepo.getShelfById(1)
        //Then
        assertThat(testShelf, `is`(shelf))
    }

    @Test
    fun `test delete non existing shelf`() = coroutineRule.runBlockingTest {
        //Given
        val shelf = Shelf("shelf", ArrayList(), "1", 1)
        //When
        viewModel.deleteShelf(shelf)
        val testShelf = internalRepo.getShelfById(1)
        //Then
        assertThat(testShelf, nullValue())
    }

}