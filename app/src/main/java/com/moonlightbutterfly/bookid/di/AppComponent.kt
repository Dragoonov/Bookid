package com.moonlightbutterfly.bookid.di

import android.content.Context
import com.moonlightbutterfly.bookid.MainActivity
import com.moonlightbutterfly.bookid.dialogs.AddBookToShelfDialog
import com.moonlightbutterfly.bookid.dialogs.AddShelfDialog
import com.moonlightbutterfly.bookid.dialogs.RenameShelfDialog
import com.moonlightbutterfly.bookid.fragments.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class, RepositoryModule::class, UtilsModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(fragment: BookFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: ShelfFragment)
    fun inject(fragment: SearchFragment)
    fun inject(fragment: LoginFragment)
    fun inject(activity: MainActivity)
    fun inject(fragment: SplashFragment)
    fun inject(fragment: BooksListFragment)
    fun inject(dialog: AddShelfDialog)
    fun inject(dialog: RenameShelfDialog)
    fun inject(dialog: AddBookToShelfDialog)
}