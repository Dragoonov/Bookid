package com.moonlightbutterfly.bookid

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.moonlightbutterfly.bookid.repository.database.entities.User
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


class UserManagerTest {

    private val mockedRepo = mockk<InternalRepository> {
        every { updateUser(any()) } returns Completable.complete()
        every { getUserById(any()) } answers { Flowable.just(listOf(User(firstArg()))) }
    }
    private val mockedCommunicator = mockk<Communicator>()

    private val userManager = UserManager(mockedRepo, mockedCommunicator)

    private val observer = Observer<User> {}

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        userManager.user.observeForever(observer)
    }

    @After
    fun cleanUp() {
        userManager.user.removeObserver(observer)
    }

    @Test
    fun `should update base shelf`() {
        //GIVEN
        val shelfId = 1
        userManager.receiveUserId("1")
        //WHEN
        userManager.updateBaseShelf(shelfId)
        //THEN
        verify {
            mockedRepo.updateUser(match {
                it.baseShelfId == shelfId
            })
        }
    }

}