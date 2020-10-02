package com.moonlightbutterfly.bookid.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.moonlightbutterfly.bookid.MainCoroutineRule
import com.moonlightbutterfly.bookid.getOrAwaitValue
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.externalrepos.ExternalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class BookViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    private lateinit var viewModel: BookViewModel
    private lateinit var externalRepo: ExternalRepository

    @Before
    fun setUp() {
        externalRepo = GoodreadRepositoryFake()
        viewModel = BookViewModel(externalRepo, Dispatchers.Main)
    }

    @Test
    fun `test set book`() = mainCoroutineRule.runBlockingTest {
        //Given
        val author = Author(2)
        val book = Book(1, author = author)
        //When
        viewModel.setBook(book)
        //Then
        assertThat(viewModel.bookLiveData.getOrAwaitValue(), `is`(book))
        assertThat(viewModel.authorBooksLiveData.getOrAwaitValue(), hasSize(4))
        assertThat(viewModel.authorInfoLiveData.getOrAwaitValue(), `is`(author))
    }

    @Test
    fun `test set book multiple times`() = mainCoroutineRule.runBlockingTest {
        //Given
        val author = Author(3)
        val book = Book(1, author = author)
        val book2 = Book(2, author = author)
        //When
        viewModel.setBook(book)
        viewModel.setBook(book2)
        //Then
        assertThat(viewModel.bookLiveData.getOrAwaitValue(), `is`(book2))
    }

    @Test
    fun `test refresh data`() = mainCoroutineRule.runBlockingTest {
        //Given
        val author = Author(2)
        val book = Book(1, author = author)
        //When
        viewModel.setBook(book)
        mainCoroutineRule.pauseDispatcher()
        viewModel.refreshData()
        //Then
        assertThat(viewModel.authorBooksLiveData.getOrAwaitValue(), nullValue())
        mainCoroutineRule.resumeDispatcher()
        assertThat(viewModel.authorBooksLiveData.getOrAwaitValue(), hasSize(4))
    }
}