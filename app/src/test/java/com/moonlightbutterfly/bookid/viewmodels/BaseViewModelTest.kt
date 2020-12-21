package com.moonlightbutterfly.bookid.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.moonlightbutterfly.bookid.Communicator
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import io.mockk.*
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

class BaseViewModelTest {

    private val observer = Observer<Shelf?> {}

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

    private val mockedCommunicator: Communicator = mockk {
        every { postMessage(any()) } just Runs
    }

    private val viewModel = BaseViewModel(
        mockedRepo,
        mockedCommunicator,
        mockk {
            every { ui() } returns Schedulers.trampoline()
            every { io() } returns Schedulers.trampoline()
        },
        mockk {
            every { user.value } returns User("1")
        })

    private val bookAddedFav = "Book added"
    private val bookRemovedFav = "Book removed"
    private val bookAddedSave = "Book added save"
    private val bookRemovedSave = "Book removed save"
    private val bookAddedDefaults = "Book added defaults"

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        viewModel.apply {
            bookRemovedFromFavouritesMessage = bookRemovedFav
            bookAddedToFavouritesMessage = bookAddedFav
            bookAddedToSavedMessage = bookAddedSave
            bookRemovedFromSavedMessage = bookRemovedSave
            bookAddedToDefaultsMessage = bookAddedDefaults
            favoriteShelfLiveData.observeForever(observer)
            savedShelfLiveData.observeForever(observer)
            baseShelfLiveData.observeForever(observer)
        }
    }

    @After
    fun cleanUp() {
        viewModel.apply {
            favoriteShelfLiveData.removeObserver(observer)
            savedShelfLiveData.removeObserver(observer)
            baseShelfLiveData.removeObserver(observer)
        }
    }

    @Test
    fun `should insert book to favourites`() {
        //GIVEN
        val book = Book("1")
        //WHEN
        viewModel.handleFavoriteOperation(book)
        //THEN
        assertTrue(viewModel.favoriteShelfLiveData.value!!.books.contains(book))
        verify(exactly = 1) {
            mockedRepo.updateShelf(viewModel.favoriteShelfLiveData.value!!)
            mockedCommunicator.postMessage(bookAddedFav)
        }
    }

    @Test
    fun `should insert and remove book from favourites`() {
        //GIVEN
        val book = Book("1")
        // WHEN
        viewModel.handleFavoriteOperation(book)
        viewModel.handleFavoriteOperation(book)
        // THEN
        assertFalse(viewModel.favoriteShelfLiveData.value!!.books.contains(book))
        verify(exactly = 2) {
            mockedRepo.updateShelf(viewModel.favoriteShelfLiveData.value!!)
        }
        verify(exactly = 1) {
            mockedCommunicator.postMessage(bookAddedFav)
            mockedCommunicator.postMessage(bookRemovedFav)
        }
    }

    @Test
    fun `should insert book to saved`() {
        //GIVEN
        val book = Book("1")
        //WHEN
        viewModel.handleSavedOperation(book)
        //THEN
        assertTrue(viewModel.savedShelfLiveData.value!!.books.contains(book))
        verify(exactly = 1) {
            mockedRepo.updateShelf(viewModel.savedShelfLiveData.value!!)
            mockedCommunicator.postMessage(bookAddedSave)
        }
    }

    @Test
    fun `should insert and remove book from saved`() {
        //GIVEN
        val book = Book("1")
        // WHEN
        viewModel.handleSavedOperation(book)
        viewModel.handleSavedOperation(book)
        // THEN
        assertFalse(viewModel.savedShelfLiveData.value!!.books.contains(book))
        verify(exactly = 2) {
            mockedRepo.updateShelf(viewModel.savedShelfLiveData.value!!)
        }
        verify(exactly = 1) {
            mockedCommunicator.postMessage(bookAddedSave)
            mockedCommunicator.postMessage(bookRemovedSave)
        }
    }

    @Test
    fun `should the book be in saved`() {
        //GIVEN
        val book = Book("1")
        // WHEN THEN
        assertFalse(viewModel.isBookInSaved(book))

        // WHEN
        viewModel.handleSavedOperation(book)

        //THEN
        assertTrue(viewModel.isBookInSaved(book))

        // WHEN
        viewModel.handleSavedOperation(book)

        //THEN
        assertFalse(viewModel.isBookInSaved(book))
    }

    @Test
    fun `should the book be in favourites`() {
        //GIVEN
        val book = Book("1")
        // WHEN THEN
        assertFalse(viewModel.isBookInFavorites(book))

        // WHEN
        viewModel.handleFavoriteOperation(book)

        //THEN
        assertTrue(viewModel.isBookInFavorites(book))

        // WHEN
        viewModel.handleFavoriteOperation(book)

        //THEN
        assertFalse(viewModel.isBookInFavorites(book))
    }

    @Test
    fun `should add book to default shelf`() {
        //GIVEN
        val book = Book("1")
        //WHEN
        viewModel.insertBookToBaseShelf(book)
        //THEN
        verify(exactly = 1) {
            mockedRepo.updateShelf(match { it.books.contains(book) })
            mockedCommunicator.postMessage(bookAddedDefaults)
        }
    }

    @Test
    fun `should not add book to default shelf`() {
        //GIVEN
        val book = null
        //WHEN
        viewModel.insertBookToBaseShelf(book)
        //THEN
        verify(exactly = 0) {
            mockedRepo.updateShelf(any())
            mockedCommunicator.postMessage(bookAddedDefaults)
        }
    }
}