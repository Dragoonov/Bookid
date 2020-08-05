package com.moonlightbutterfly.bookid.repository.database.daos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.moonlightbutterfly.bookid.AndroidMainCoroutineRule
import com.moonlightbutterfly.bookid.repository.database.AppDatabase
import com.moonlightbutterfly.bookid.repository.database.entities.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class UserDaoTest {

    private lateinit var database: AppDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = AndroidMainCoroutineRule()


    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun test_insert_and_get_user() = mainCoroutineRule.runBlockingTest {
        //Given
        val user = User("id")
        val dao = database.userDao()
        //When
        dao.insertUser(user)
        var testUser: User? = null
        val job = launch {
            dao.getUser().collect { testUser = it }
        }
        job.cancel()
        //Then
        assertThat(testUser, `is`(user))
    }

    @Test
    fun test_delete_and_get_user() = mainCoroutineRule.runBlockingTest {
        //Given
        val user = User("id")
        val dao = database.userDao()
        //When
        dao.insertUser(user)
        dao.deleteUser(user)
        var testUser: User? = null
        val job = launch {
            dao.getUser().collect { testUser = it }
        }
        job.cancel()
        //Then
        assertThat(testUser, nullValue())
    }
}