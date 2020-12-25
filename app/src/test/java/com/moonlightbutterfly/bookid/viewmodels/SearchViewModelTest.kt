package com.moonlightbutterfly.bookid.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import com.moonlightbutterfly.bookid.repository.externalrepos.ExternalRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class SearchViewModelTest {

    private val observer = Observer<List<Book>> {}

    private val externalRepoMock = mockk<ExternalRepository> {
        every { loadSearchedBooks(any(), any()) } returns Single.just(listOf(
            Book("id")
        ))
    }

    private lateinit var viewModel: SearchViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        viewModel = SearchViewModel(
            externalRepoMock,
            mockk {
                every { ui() } returns Schedulers.trampoline()
                every { io() } returns Schedulers.trampoline()
            },
            mockk {
                every { getShelfById(any(), any()) } returns Flowable.just(Shelf())
                every { getShelfByBaseId(any(), any()) } returns Flowable.just(Shelf())
            },
            mockk {
                every { user.value } returns User("1")
            },
            mockk()
        )
        viewModel.allBooks.observeForever(observer)
    }

    @After
    fun cleanUp() {
        viewModel.allBooks.removeObserver(observer)
    }

    @Test
    fun `should request search`() {
        //GIVEN
        val query = "query"
        //WHEN
        viewModel.requestSearch(query)
        //THEN
        verify {
            externalRepoMock.loadSearchedBooks(query, any())
        }
        assertTrue(viewModel.allBooks.value!!.contains(Book("id")))
        assertTrue(viewModel.allBooks.value!!.size == 1)
    }

    @Test
    fun `should not request search`() {
        //GIVEN
        val query = ""
        //WHEN
        viewModel.requestSearch(query)
        //THEN
        verify(exactly = 0) {
            externalRepoMock.loadSearchedBooks(query, any())
        }
        assertFalse(viewModel.allBooks.value!!.contains(Book("id")))
        assertTrue(viewModel.allBooks.value!!.size == 0)
    }

    @Test
    fun `should load more`() {
        //GIVEN
        val query = "query"
        //WHEN
        viewModel.requestSearch(query)
        viewModel.loadMore()
        //THEN
        verify(exactly = 2) {
            externalRepoMock.loadSearchedBooks(query, any())
        }
        assertTrue(viewModel.allBooks.value!!.contains(Book("id")))
        assertTrue(viewModel.allBooks.value!!.size == 2)
    }
}