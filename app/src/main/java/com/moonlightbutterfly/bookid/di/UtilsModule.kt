package com.moonlightbutterfly.bookid.di

import com.moonlightbutterfly.bookid.Communicator
import com.moonlightbutterfly.bookid.CommunicatorImpl
import com.moonlightbutterfly.bookid.Manager
import com.moonlightbutterfly.bookid.UserManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
abstract class UtilsModule {

    @Binds
    abstract fun bindCommunicator(x: CommunicatorImpl): Communicator

    @Binds
    abstract fun bindManager(x: UserManager): Manager

    companion object {
        @Provides
        fun provideDispatcher(): CoroutineDispatcher = Dispatchers.Main
    }
}