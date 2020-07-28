package com.moonlightbutterfly.bookid.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.moonlightbutterfly.bookid.Manager
import com.moonlightbutterfly.bookid.ManagerFake
import com.moonlightbutterfly.bookid.repository.database.entities.User
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import com.moonlightbutterfly.bookid.repository.internalrepo.RoomRepositoryFake
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class ShelfViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ShelfViewModel
    private lateinit var manager: Manager
    private lateinit var internalRepo: InternalRepository

    @Before
    fun setUp() {
        manager = ManagerFake().apply {
            signInUser(
                User(
                    "1",
                    "testNick",
                    "testMail",
                    ""
                )
            )
        }
        internalRepo = RoomRepositoryFake()
        viewModel = ShelfViewModel(manager, internalRepo)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() = Dispatchers.resetMain()


    @Test
    fun `test insert shelf`() = runBlockingTest {
        //Given
        val shelfName = "shelf1"
        //When
        viewModel.insertShelf(shelfName)
        //Then
        assertThat(
            internalRepo.getShelfByName(shelfName),
            CoreMatchers.notNullValue()
        )
    }

    @Test
    fun `test insert null shelf`() = runBlockingTest {
        //Given
        val shelfName: String? = null
        //When
        viewModel.insertShelf(shelfName)
        //Then
        internalRepo.getUserShelfs("1").collect {
            assertThat(it, nullValue())
        }
    }
}