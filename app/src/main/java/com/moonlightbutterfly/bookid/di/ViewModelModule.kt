package com.moonlightbutterfly.bookid.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moonlightbutterfly.bookid.viewmodels.BookViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewHolderFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(BookViewModel::class)
    internal abstract fun bindBookViewModel(viewModel: BookViewModel): ViewModel
}