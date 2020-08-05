package com.moonlightbutterfly.bookid.repository.database.daos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.moonlightbutterfly.bookid.AndroidMainCoroutineRule
import com.moonlightbutterfly.bookid.repository.database.AppDatabase
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.Matchers.hasSize
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ShelfDaoTest {

    private lateinit var database: AppDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = AndroidMainCoroutineRule()


    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            AppDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun test_insert_shelf_and_get_by_id() = mainCoroutineRule.runBlockingTest {
        //Given
        val id = 1
        val dao = database.shelfDao()
        val shelf = Shelf(id, "test", ArrayList(), "testId")
        //When
        dao.insertShelf(shelf)
        val testShelf = dao.getShelfById(id)
        //Then
        assertThat(testShelf, `is`(shelf))
    }

    @Test
    fun test_insert_shelf_and_get_by_name() = mainCoroutineRule.runBlockingTest {
        //Given
        val name = "test"
        val dao = database.shelfDao()
        val shelf = Shelf(1, name, ArrayList(), "testId")
        //When
        dao.insertShelf(shelf)
        val testShelf = dao.getShelfByName(name)
        //Then
        assertThat(testShelf, `is`(shelf))
    }

    @Test
    fun test_update_shelf_and_get_by_id() = mainCoroutineRule.runBlockingTest {
        //Given
        val testName = "test2"
        val dao = database.shelfDao()
        val shelf = Shelf(1, "test", ArrayList(), "testId")
        //When
        dao.insertShelf(shelf)
        shelf.name = testName
        dao.updateShelf(shelf)
        val testShelf = dao.getShelfById(1)
        //Then
        assertThat(testShelf.name, `is`(testName))
    }

    @Test
    fun test_delete_shelf_and_get_by_id() = mainCoroutineRule.runBlockingTest {
        //Given
        val dao = database.shelfDao()
        val shelf = Shelf(1, "test", ArrayList(), "testId")
        //When
        dao.insertShelf(shelf)
        dao.deleteShelf(shelf)
        val testShelf = dao.getShelfById(1)
        //Then
        assertThat(testShelf, nullValue())
    }

    @Test
    fun test_insert_shelfs_and_get_list_of_them() = mainCoroutineRule.runBlockingTest {
        //Given
        val dao = database.shelfDao()
        val shelf = Shelf(1, "test", ArrayList(), "testId")
        val shelf2 = Shelf(2, "test", ArrayList(), "testId")
        val shelf3 = Shelf(3, "test", ArrayList(), "testId")
        //When
        dao.apply {
            insertShelf(shelf)
            insertShelf(shelf2)
            insertShelf(shelf3)
        }
        val testShelfs = dao.getShelfs()
        //Then
        assertThat(testShelfs, hasSize(3))
    }

    @Test
    fun test_insert_shelfs_and_get_them_by_user_id() = mainCoroutineRule.runBlockingTest {
        //Given
        val testId = "testId"
        val dao = database.shelfDao()
        val shelf = Shelf(1, "test", ArrayList(), testId)
        val shelf2 = Shelf(2, "test", ArrayList(), testId)
        val shelf3 = Shelf(3, "test", ArrayList(), "testId2")
        //When
        dao.apply {
            insertShelf(shelf)
            insertShelf(shelf2)
            insertShelf(shelf3)
        }
        var testShelfs: List<Shelf>? = null
        val job = launch {
            dao.getUserShelfs(testId).collect { testShelfs = it }
        }
        job.cancel()
        //Then
        assertThat(testShelfs, hasSize(2))
    }
}
