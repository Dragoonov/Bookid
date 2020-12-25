package com.moonlightbutterfly.bookid.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class BooksListViewModelTest {

    private val observer = Observer<Shelf?> {}
    private val viewModel = BooksListViewModel(
        mockk {
            every { getShelfById(eq(5, true), any()) } returns Flowable.just(Shelf().apply { books = listOf(Book("1")) })
            every { getShelfById(eq(5), any()) } returns Flowable.just(Shelf().apply { books = listOf(Book("5")) })
            every { getShelfByBaseId(any(), any()) } returns Flowable.just(Shelf())
            every { updateShelf(any()) } returns Completable.complete()
        },
        mockk {
            every { user.value } returns User("1")
        },
        mockk {
            every { ui() } returns Schedulers.trampoline()
            every { io() } returns Schedulers.trampoline()
        },
        mockk())

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        viewModel.customShelfLiveData.observeForever(observer)
    }

    @After
    fun cleanUp() {
        viewModel.customShelfLiveData.removeObserver(observer)
    }

    @Test
    fun `should delete book from custom shelf`() {
        //GIVEN
        val book = Book("1")
        assertTrue(viewModel.customShelfLiveData.value!!.books.contains(book))
        //WHEN
        viewModel.deleteBookFromCustom(book)
        //THEN
        assertFalse(viewModel.customShelfLiveData.value!!.books.contains(book))
    }

    @Test
    fun `should set custom shelf id`() {
        //GIVEN
        val id = 5
        val book = Book("5")
        //WHEN
        viewModel.setCustomShelfId(id)
        //THEN
        assertTrue(viewModel.customShelfLiveData.value!!.books.contains(book))
    }

}