package com.moonlightbutterfly.bookid

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


class CommunicatorImplTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var communicatorImpl: CommunicatorImpl

    @Before
    fun initializeCommunicator() {
        communicatorImpl = CommunicatorImpl()
    }

    @Test
    fun `test posting single message`() {
        //Given
        val message = "message"
        //When
        communicatorImpl.postMessage(message)
        //Then
        assertThat(communicatorImpl.message.getOrAwaitValue(), allOf(not(isEmptyString())))
    }

    @Test
    fun `test clearing the message`() {
        //Given
        val message = "message"
        //When
        communicatorImpl.postMessage(message)
        communicatorImpl.clearMessage()
        //Then
        assertThat(communicatorImpl.message.getOrAwaitValue(), nullValue())
    }

    @Test
    fun `test clearing empty communicator`() {
        //When
        communicatorImpl.clearMessage()
        communicatorImpl.clearMessage()
        //Then
        assertThat(communicatorImpl.message.getOrAwaitValue(), nullValue())
    }

    @Test
    fun `test posting multiple messages`() {
        //Given
        val message1 = "message1"
        val message2 = "message2"
        //When
        communicatorImpl.run {
            postMessage(message1)
            postMessage(message2)
        }
        //Then
        assertThat(communicatorImpl.message.getOrAwaitValue(), equalTo(message2))
    }
}