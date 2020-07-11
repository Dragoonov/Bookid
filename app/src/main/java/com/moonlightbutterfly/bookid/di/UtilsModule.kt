package com.moonlightbutterfly.bookid.di

import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Module
object UtilsModule {

    @Provides
    fun provideExecutor(): Executor = Executors.newSingleThreadExecutor()

}