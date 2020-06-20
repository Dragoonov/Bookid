package com.moonlightbutterfly.bookid.di

import android.content.Context
import com.moonlightbutterfly.bookid.fragments.BookFragment
import com.moonlightbutterfly.bookid.fragments.ProfileFragment
import com.moonlightbutterfly.bookid.fragments.SearchFragment
import com.moonlightbutterfly.bookid.fragments.ShelfFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class, RepositoryModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(fragment: BookFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: ShelfFragment)
    fun inject(fragment: SearchFragment)
}