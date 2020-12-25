package com.moonlightbutterfly.bookid.viewmodels

import androidx.room.EmptyResultSetException
import com.moonlightbutterfly.bookid.Communicator
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import com.moonlightbutterfly.bookid.utils.Logos
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertTrue
import org.junit.Test

class ShelfsViewModelTest {

    private val internalRepoMock = mockk<InternalRepository> {
        every { getUserShelfs(any()) } returns Flowable.just(listOf())
        every { doBaseShelfsExist(any(), any()) } returns Single.error(EmptyResultSetException(""))
        every { deleteShelf(any()) } returns Completable.complete()
        every { updateShelf(any()) } returns Completable.complete()
        every { insertShelf(any()) } returns Completable.complete()
    }

    private val communicatorMock = mockk<Communicator>{
        every { postMessage(any()) } just Runs
    }

    private val viewModel = ShelfsViewModel(
        mockk {
            every { user.value } returns User("1")
        },
        internalRepoMock,
        communicatorMock,
        mockk {
            every { ui() } returns Schedulers.trampoline()
            every { io() } returns Schedulers.trampoline()
        }
    )

    @Test
    fun `should prepare basic shelfs`() {
        //GIVEN
        val names = arrayOf("1", "2", "3")
        val images = arrayOf(
            Pair(1,Logos.BALL),
            Pair(2, Logos.BALL),
            Pair(3, Logos.BALL)
        )
        //WHEN
        viewModel.prepareBasicShelfs(names, images)

        verifyOrder {
            internalRepoMock.insertShelf(match { it.name == names[0] })
            internalRepoMock.insertShelf(match { it.name == names[1] })
            internalRepoMock.insertShelf(match { it.name == names[2] })
        }
    }

    @Test
    fun `should delete shelf`() {
        //GIVEN
        val shelf = Shelf()
        //WHEN
        viewModel.deleteShelf(shelf)
        //THEN
        verify {
            internalRepoMock.deleteShelf(shelf)
        }
    }

    @Test
    fun `should insert book to shelf`() {
        //GIVEN
        val book = Book("1")
        val shelf = Shelf()
        val message = "message"
        //WHEN
        viewModel.insertBookToShelf(shelf, book, message)
        //THEN
        assertTrue(shelf.books.contains(book))
        verify(exactly = 1) {
            internalRepoMock.updateShelf(shelf)
            communicatorMock.postMessage(message)
        }
    }

}