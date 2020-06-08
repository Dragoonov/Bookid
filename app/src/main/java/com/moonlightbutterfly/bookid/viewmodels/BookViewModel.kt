package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.externalrepos.ExternalRepository
import javax.inject.Inject

class BookViewModel @Inject constructor(val repository: ExternalRepository) : ViewModel() {

    private val _similarBooksLiveData = MutableLiveData<List<Book>>()
    val similarBooksLiveData: LiveData<List<Book>> get() = _similarBooksLiveData

    private val _authorBooksLiveData = MutableLiveData<List<Book>>()
    val authorBooksLiveData: LiveData<List<Book>> get() = _authorBooksLiveData

    private val _authorInfoLiveData = MutableLiveData<Author>()
    val authorInfoLiveData: LiveData<Author> get() = _authorInfoLiveData

    private var _bookLiveData = MutableLiveData<Book>()
    val bookLiveData: LiveData<Book> get() = _bookLiveData

    val allDataLoaded: MutableLiveData<Boolean> = MutableLiveData(false)

    private val similarBooksLiveDataObserver =
        Observer<List<Book>> { _similarBooksLiveData.value = it }
    private val authorBooksLiveDataObserver =
        Observer<List<Book>> { _authorBooksLiveData.value = it }
    private val authorInfoLiveDataObserver = Observer<Author> { _authorInfoLiveData.value = it }

    init {
        subscribeToRepository()
    }

    fun setBook(book: Book) {
        if( _bookLiveData.value?.id == book.id) {
            return
        }
        this._bookLiveData = MutableLiveData(book)
        refreshData()
    }

    fun updateDataLoaded() {
        allDataLoaded.value = authorInfoLiveData.value != null && authorBooksLiveData.value != null
    }

    fun removeDisplayedBookFromList(): List<Book>? = _authorBooksLiveData.value
        ?.toMutableList()
        ?.filter { it.id != _bookLiveData.value?.id }

    fun refreshData() {
        clearCurrentData()
        repository.loadSimilarBooks(_bookLiveData.value!!)
        repository.loadAuthorBooks(_bookLiveData.value?.author!!)
        repository.loadAuthorInfo(_bookLiveData.value?.author!!)
    }

    private fun clearCurrentData() {
        _similarBooksLiveData.value = null
        _authorBooksLiveData.value = null
        _authorInfoLiveData.value = null
    }

    override fun onCleared() {
        super.onCleared()
        unsubscribeFromRepository()
    }

    private fun subscribeToRepository() {
        repository.authorBooksLiveData.observeForever(authorBooksLiveDataObserver)
        repository.authorInfoLiveData.observeForever(authorInfoLiveDataObserver)
        repository.similarBooksLiveData.observeForever(similarBooksLiveDataObserver)
    }

    private fun unsubscribeFromRepository() {
        repository.authorBooksLiveData.removeObserver(authorBooksLiveDataObserver)
        repository.authorInfoLiveData.removeObserver(authorInfoLiveDataObserver)
        repository.similarBooksLiveData.removeObserver(similarBooksLiveDataObserver)
    }

}