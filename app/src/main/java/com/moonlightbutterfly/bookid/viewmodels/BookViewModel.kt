package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.*
import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.externalrepos.ExternalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

fun List<Book>.removeDisplayedBookFromList(book: Book): List<Book>? = this
    .toMutableList()
    .filter { it.id != book.id }

class BookViewModel @Inject constructor(val repository: ExternalRepository) : ViewModel() {

    private val _authorBooksLiveData = liveData {
        val books = repository
            .loadAuthorBooks(_bookLiveData.value?.author!!)
            .removeDisplayedBookFromList(_bookLiveData.value as Book)
        emit(books)
    } as MutableLiveData
    val authorBooksLiveData: LiveData<List<Book>?> get() = _authorBooksLiveData

    private val _authorInfoLiveData = liveData {
        val author = repository.loadAuthorInfo(_bookLiveData.value?.author!!)
        emit(author)
    } as MutableLiveData
    val authorInfoLiveData: LiveData<Author> get() = _authorInfoLiveData

    private var _bookLiveData = MutableLiveData<Book>()
    val bookLiveData: LiveData<Book> get() = _bookLiveData

    private val _allDataLoaded: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(authorInfoLiveData) { value = updateDataLoaded() }
        addSource(authorBooksLiveData) { value = updateDataLoaded() }
    }
    val allDataLoaded: LiveData<Boolean> get() = _allDataLoaded

    fun setBook(book: Book) {
        if (_bookLiveData.value?.id == book.id) {
            return
        }
        this._bookLiveData.value = book
        refreshData()
    }

    private fun updateDataLoaded(): Boolean =
        authorInfoLiveData.value != null && authorBooksLiveData.value != null


    fun refreshData() {
        clearCurrentData()
        viewModelScope.launch {
            val books = repository
                .loadAuthorBooks(_bookLiveData.value?.author!!)
                .removeDisplayedBookFromList(_bookLiveData.value as Book)
            _authorBooksLiveData.value = books
            _authorInfoLiveData.value = repository.loadAuthorInfo(_bookLiveData.value?.author!!)
        }
    }

    private fun clearCurrentData() {
        _authorBooksLiveData.value = null
        _authorInfoLiveData.value = null
    }

}