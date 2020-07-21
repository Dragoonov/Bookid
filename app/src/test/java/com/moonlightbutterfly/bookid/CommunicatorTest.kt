package com.moonlightbutterfly.bookid

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.hamcrest.Matchers.*
import org.junit.After


class CommunicatorTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var communicator: Communicator
    private var observedMessage: String? = null
    private val observer = Observer<String?> {observedMessage = it}

    @Before
    fun init() {
        communicator = Communicator().apply {
            message.observeForever(observer)
        }
    }

    @After
    fun clear() {
        communicator.message.removeObserver(observer)
    }

    @Test
    fun `test posting single message`() {
        //Given
        val message = "message"
        communicator.message.observeForever(observer)
        //When
        communicator.postMessage(message)
        //Then
        assertThat(communicator.message.value, allOf(not(isEmptyString()), equalTo(observedMessage)))
    }

    @Test
    fun `test clearing the message`() {
        //Given
        val message = "message"
        //When
        communicator.postMessage(message)
        communicator.clearMessage()
        //Then
        assertThat(communicator.message.value, nullValue())
    }

    @Test
    fun `test clearing empty communicator`() {
        //When
        communicator.clearMessage()
        communicator.clearMessage()
        //Then
        assertThat(communicator.message.value, nullValue())
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
        assertThat(observedMessage, equalTo(message2))
    }
}