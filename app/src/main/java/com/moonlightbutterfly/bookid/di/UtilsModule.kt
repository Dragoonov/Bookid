package com.moonlightbutterfly.bookid.di

import com.moonlightbutterfly.bookid.*
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

    @Binds
    abstract fun bindScheduler(x: SchedulerProviderImpl): SchedulerProvider

    companion object {
        @Provides
        fun provideDispatcher(): CoroutineDispatcher = Dispatchers.Main
    }
}