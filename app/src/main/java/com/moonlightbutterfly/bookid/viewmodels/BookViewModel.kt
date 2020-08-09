package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.*
import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.externalrepos.ExternalRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class BookViewModel @Inject constructor(
    private val repository: ExternalRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private var _bookLiveData = MutableLiveData<Book>()
    val bookLiveData: LiveData<Book> get() = _bookLiveData

    fun setBook(book: Book) {
        if (_bookLiveData.value?.id == book.id) {
            return
        }
        this._bookLiveData.value = book
    }





}