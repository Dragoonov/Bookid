package com.moonlightbutterfly.bookid.viewmodels

import com.moonlightbutterfly.bookid.Manager
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import io.mockk.*
import io.reactivex.Flowable
import org.junit.Test

class ProfileViewModelTest {

    private val userManagerMock = mockk<Manager> {
        every { updateBaseShelf(any()) } just Runs
        every { user.value } returns User("1")
    }

    private val viewModel = ProfileViewModel(
        userManagerMock,
        mockk {
            every { getShelfById(any(), any()) } returns Flowable.just(Shelf())
            every { getUserShelfs(any())} returns Flowable.just(listOf())
        }
    )

    @Test
    fun `should update base shelf`() {
        //GIVEN
        val id = 1
        //WHEN
        viewModel.updateBaseShelf(id)
        //THEN
        verify {
            userManagerMock.updateBaseShelf(id)
        }
    }
}