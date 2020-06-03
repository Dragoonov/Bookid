package com.moonlightbutterfly.bookid.di

import android.content.Context
import com.moonlightbutterfly.bookid.BookFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(fragment: BookFragment)
}