package com.moonlightbutterfly.bookid.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.moonlightbutterfly.bookid.MainCoroutineRule
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.externalrepos.ExternalRepository
import com.moonlightbutterfly.bookid.repository.externalrepos.goodreads.GoodreadRepositoryFake
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasSize
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    private lateinit var viewModel: SearchViewModel
    private lateinit var externalRepo: ExternalRepository
    private val observer: Observer<List<Book>> = Observer {  }

    @Before
    fun setUp() {
        externalRepo = GoodreadRepositoryFake()
        viewModel = SearchViewModel(externalRepo, Dispatchers.Main)
        viewModel.allBooks.observeForever(observer)
    }

    @After
    fun tearDown() {
        viewModel.allBooks.removeObserver(observer)
    }

    @Test
    fun `test request search`() = mainCoroutineRule.runBlockingTest {
        //Given
        val searchString = "test"
        //When
        viewModel.requestSearch(searchString)
        //Then
        assertThat(viewModel.allBooks.value, hasSize(4))
    }

    @Test
    fun `test request search multiple times`() = mainCoroutineRule.runBlockingTest {
        //Given
        val searchString = "test"
        //When
        viewModel.requestSearch(searchString)
        viewModel.requestSearch(searchString)
        viewModel.requestSearch(searchString)
        //Then
        assertThat(viewModel.allBooks.value, hasSize(4))
    }

    @Test
    fun `test request search with null value`() = mainCoroutineRule.runBlockingTest {
        //Given
        val searchString = null
        //When
        viewModel.requestSearch(searchString)
        //Then
        assertThat(viewModel.allBooks.value, hasSize(0))
    }

    @Test
    fun `test request search with empty value`() = mainCoroutineRule.runBlockingTest {
        //Given
        val searchString = ""
        //When
        viewModel.requestSearch(searchString)
        //Then
        assertThat(viewModel.allBooks.value, hasSize(0))
    }

    @Test
    fun `test load more`() = mainCoroutineRule.runBlockingTest {
        //Given
        val searchString = "test"
        //When
        viewModel.requestSearch(searchString)
        viewModel.loadMore()
        viewModel.loadMore()
        //Then
        assertThat(viewModel.allBooks.value, hasSize(12))
    }

}