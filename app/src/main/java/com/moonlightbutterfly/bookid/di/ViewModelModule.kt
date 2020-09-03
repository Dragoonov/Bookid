package com.moonlightbutterfly.bookid.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moonlightbutterfly.bookid.viewmodels.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(BookViewModel::class)
    internal abstract fun bindBookViewModel(viewModel: BookViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShelfViewModel::class)
    internal abstract fun bindShelfViewModel(viewModel: ShelfViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    internal abstract fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BooksListViewModel::class)
    internal abstract fun bindBooksListViewModel(viewModel: BooksListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateEditShelfViewModel::class)
    internal abstract fun bindCreateEditShelfViewModel(viewModel: CreateEditShelfViewModel): ViewModel
}