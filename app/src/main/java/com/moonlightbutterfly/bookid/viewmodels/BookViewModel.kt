package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moonlightbutterfly.bookid.repository.database.entities.Book

class BookViewModel : ViewModel() {
    lateinit var book: MutableLiveData<Book>
}
