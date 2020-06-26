package com.moonlightbutterfly.bookid.di

import android.content.Context
import com.moonlightbutterfly.bookid.MainActivity
import com.moonlightbutterfly.bookid.fragments.*
import dagger.BindsInstance
import dagger.Component
import dagger.Provides
import java.util.concurrent.Executor
import java.util.concurrent.Executors
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
    fun inject(fragment: EditShelfsFragment)
    fun inject(fragment: LoginFragment)
    fun inject(activity: MainActivity)
}