package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.*
import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.externalrepos.ExternalRepository
import javax.inject.Inject

class BookViewModel @Inject constructor(val repository: ExternalRepository) : ViewModel() {

    private val _authorBooksLiveData = Transformations.switchMap(repository.authorBooksLiveData)
    { MutableLiveData(removeDisplayedBookFromList(it)) } as MutableLiveData<List<Book>?>
    val authorBooksLiveData: LiveData<List<Book>?> get() = _authorBooksLiveData

    private val _authorInfoLiveData = Transformations.switchMap(repository.authorInfoLiveData)
    { MutableLiveData(it) } as MutableLiveData<Author>
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


    private fun removeDisplayedBookFromList(books: List<Book>?): List<Book>? = books
        ?.toMutableList()
        ?.filter { it.id != _bookLiveData.value?.id }

    fun refreshData() {
        clearCurrentData()
        repository.loadAuthorBooks(_bookLiveData.value?.author!!)
        repository.loadAuthorInfo(_bookLiveData.value?.author!!)
    }

    private fun clearCurrentData() {
        _authorBooksLiveData.value = null
        _authorInfoLiveData.value = null
    }

}