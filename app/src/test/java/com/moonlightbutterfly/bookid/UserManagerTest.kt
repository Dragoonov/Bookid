package com.moonlightbutterfly.bookid

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.moonlightbutterfly.bookid.repository.database.entities.User
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


class UserManagerTest {

    private val mockedRepo = mockk<InternalRepository> {
        every { updateUser(any()) } returns Completable.complete()
        every { getUserById(eq("test", true)) } answers { Flowable.just(listOf()) }
        every { getUserById(eq("test")) } answers { Flowable.just(listOf(User(firstArg()))) }
        every { insertUser(any()) } returns Completable.complete()
    }
    private val mockedCommunicator = mockk<Communicator>()

    private val userManager = UserManager(
        mockedRepo,
        mockedCommunicator,
        mockk {
            every { ui() } returns Schedulers.trampoline()
            every { io() } returns Schedulers.trampoline()
        })

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

    @Test
    fun `should sign in user`() {
        //GIVEN
        val user = User("1")
        val preferencesEditorMock: SharedPreferences.Editor = mockk(relaxed = true)
        val preferencesMock: SharedPreferences = mockk {
            every { edit() } returns preferencesEditorMock
        }
        val contextMock: Context = mockk {
            every { getSharedPreferences(any(), any()) } returns preferencesMock
        }
        //WHEN
        userManager.signInUser(user,contextMock)
        //THEN
        verify {
            mockedRepo.insertUser(match { it.id == user.id })
            preferencesEditorMock.putString(
                match { it == UserManager.ID_KEY },
                match { it == user.id })
        }
    }

    @Test fun `should receive user id and set user accordingly`() {
        //GIVEN
        val id = "test"
        //WHEN
        userManager.receiveUserId(id)
        //THEN
        assertEquals(id,userManager.user.value!!.id)
    }

}