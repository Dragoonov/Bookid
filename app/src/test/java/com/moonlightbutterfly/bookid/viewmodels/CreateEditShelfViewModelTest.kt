package com.moonlightbutterfly.bookid.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import com.moonlightbutterfly.bookid.utils.Logos
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import io.mockk.verifySequence
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class CreateEditShelfViewModelTest {

    private val shelfObserver = Observer<Shelf?> {}
    private val observerActionTitle = Observer<String> {}

    private val mockedRepo: InternalRepository = mockk {
        every { updateShelf(any()) } returns Completable.complete()
        every { insertShelf(any()) } returns Completable.complete()
        every { getShelfById(any(), any()) } returns Flowable.just(Shelf())
    }

    private val viewModel = CreateEditShelfViewModel(
        mockedRepo,
        mockk {
            every { user.value } returns User("1")
        },
        mockk(),
        mockk {
            every { ui() } returns Schedulers.trampoline()
            every { io() } returns Schedulers.trampoline()
        }
    )

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        viewModel.shelfLiveData.observeForever(shelfObserver)
        viewModel.actionTitle.observeForever(observerActionTitle)
    }

    @After
    fun cleanUp() {
        viewModel.shelfLiveData.removeObserver(shelfObserver)
        viewModel.actionTitle.removeObserver(observerActionTitle)
    }

    @Test
    fun `should set action title`() {
        //GIVEN
        val actionTitle = "title"
        //WHEN
        viewModel.setActionTitle(actionTitle)
        //THEN
        assertEquals(actionTitle, viewModel.actionTitle.value)
    }

    @Test
    fun `should save and then update shelf`() {
        //GIVEN
        val name = ""
        val background = 1
        val iconId = Logos.BALL
        //WHEN
        viewModel.finishCreateModify(name, background, iconId)
        viewModel.setShelfId(1)
        viewModel.finishCreateModify(name, background, iconId)
        //THEN
        verifyOrder {
            mockedRepo.insertShelf(any())
            mockedRepo.updateShelf(any())
        }
    }
}