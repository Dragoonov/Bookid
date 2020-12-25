package com.moonlightbutterfly.bookid.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class BookViewModelTest {

    private val observer = Observer<Book> {}
    private val observerExpanded = Observer<Boolean> {}

    private val mockedRepo: InternalRepository = mockk {
        every { updateShelf(any()) } returns Completable.complete()
        every {
            getShelfByBaseId(
                any(),
                any()
            )
        } returns Flowable.just(Shelf())
        every { getShelfById(any(), any()) } returns Flowable.just(Shelf())
    }

    private val viewModel = BookViewModel(
        mockk(), mockedRepo,
        mockk {
            every { user.value } returns User("1")
        },
        mockk {
            every { ui() } returns Schedulers.trampoline()
            every { io() } returns Schedulers.trampoline()
        },
        mockk()
    )

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        viewModel.bookLiveData.observeForever(observer)
        viewModel.descriptionExpandedMode.observeForever(observerExpanded)
    }

    @After
    fun cleanUp() {
        viewModel.bookLiveData.removeObserver(observer)
        viewModel.descriptionExpandedMode.removeObserver(observerExpanded)
    }

    @Test
    fun `should set book`() {
        //GIVEN
        val book = Book("1")
        val book2 = Book("2")
        //WHEN
        viewModel.setBook(book)
        //THEN
        assertEquals(book, viewModel.bookLiveData.value)

        //WHEN
        viewModel.setBook(book2)
        //THEN
        assertEquals(book2, viewModel.bookLiveData.value)
        verify(exactly = 1) {
            mockedRepo.updateShelf(any())
        }
    }

    @Test
    fun `should set expanded mode`() {
        //GIVEN
        assertFalse(viewModel.descriptionExpandedMode.value!!)
        //WHEN
        viewModel.changeExpanded()
        //THEN
        assertTrue(viewModel.descriptionExpandedMode.value!!)
        //WHEN
        viewModel.changeExpanded()
        //THEN
        assertFalse(viewModel.descriptionExpandedMode.value!!)
    }

}