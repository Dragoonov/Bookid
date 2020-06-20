package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.*
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.externalrepos.ExternalRepository
import org.simpleframework.xml.transform.Transform
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val externalRepository: ExternalRepository) : ViewModel() {

    val searchedBooks: LiveData<List<Book>> get() = _searchedBooks
    private var _searchedBooks: MutableLiveData<List<Book>> = (Transformations
        .switchMap(externalRepository.searchedBooksLiveData) {MutableLiveData(it)}
            as MutableLiveData<List<Book>>).also {
        it.value = ArrayList()
    }

    private var page = 1

    val allDataLoaded: LiveData<Boolean> = Transformations.map(searchedBooks) {
        it != null
    }

    fun clearData() {
        if(_searchedBooks.value != null) {
            _searchedBooks.value = null
        }
    }

    fun requestSearch(query: String?) {
        if(!query.isNullOrEmpty()) {
            externalRepository.loadSearchedBooks(query, page)
        }
    }

    fun clearPage() {
        page = 1
    }
}