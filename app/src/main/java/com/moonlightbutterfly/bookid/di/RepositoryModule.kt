package com.moonlightbutterfly.bookid.di

import com.moonlightbutterfly.bookid.repository.externalrepos.ExternalRepository
import com.moonlightbutterfly.bookid.repository.externalrepos.goodreads.GoodreadsRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun provideRepository(x: GoodreadsRepository): ExternalRepository
}