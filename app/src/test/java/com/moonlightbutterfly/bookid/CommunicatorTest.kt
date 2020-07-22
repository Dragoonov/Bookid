package com.moonlightbutterfly.bookid

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


class CommunicatorTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var communicator: Communicator

    @Before
    fun initializeCommunicator() {
        communicator = Communicator()
    }

    @Test
    fun `test posting single message`() {
        //Given
        val message = "message"
        //When
        communicator.postMessage(message)
        //Then
        assertThat(communicator.message.getOrAwaitValue(), allOf(not(isEmptyString())))
    }

    @Test
    fun `test clearing the message`() {
        //Given
        val message = "message"
        //When
        communicator.postMessage(message)
        communicator.clearMessage()
        //Then
        assertThat(communicator.message.getOrAwaitValue(), nullValue())
    }

    @Test
    fun `test clearing empty communicator`() {
        //When
        communicator.clearMessage()
        communicator.clearMessage()
        //Then
        assertThat(communicator.message.getOrAwaitValue(), nullValue())
    }

    @Test
    fun `test posting multiple messages`() {
        //Given
        val message1 = "message1"
        val message2 = "message2"
        //When
        communicator.run {
            postMessage(message1)
            postMessage(message2)
        }
        //Then
        assertThat(communicator.message.getOrAwaitValue(), equalTo(message2))
    }
}