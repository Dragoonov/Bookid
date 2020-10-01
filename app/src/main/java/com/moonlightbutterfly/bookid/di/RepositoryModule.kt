package com.moonlightbutterfly.bookid.di

import com.moonlightbutterfly.bookid.repository.externalrepos.ExternalRepository
import com.moonlightbutterfly.bookid.repository.externalrepos.googlebooks.GoogleBooksRepository
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import com.moonlightbutterfly.bookid.repository.internalrepo.RoomRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun provideExternalRepository(x: GoogleBooksRepository): ExternalRepository

    @Binds
    abstract fun provideInternalRepository(x: RoomRepository): InternalRepository

}