package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.*
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.externalrepos.ExternalRepository
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val externalRepository: ExternalRepository) : ViewModel() {

    private var searchedBooks: MutableLiveData<List<Book>> = (Transformations
        .switchMap(externalRepository.searchedBooksLiveData) {
            MutableLiveData(it)
        } as MutableLiveData<List<Book>>).also {
        it.value = ArrayList()
    }
    val allBooks = MediatorLiveData<MutableList<Book>>().apply {
        value = ArrayList()
        addSource(searchedBooks) {
            if (it != null) {
                value = ArrayList<Book>().apply {
                    addAll(value as ArrayList<Book>)
                    addAll((it))
                }
            }
        }
    }
    var showHint = true
    
    var currentQuery: String? = ""

    private var page = 1

    val allDataLoaded: LiveData<Boolean> = Transformations.map(searchedBooks) {
        searchedBooks.value != null
    }

    fun clearData() {
        searchedBooks.value = null
    }

    fun requestSearch(query: String?) {
        allBooks.value?.clear()
        page = 1
        if(!query.isNullOrEmpty()) {
            externalRepository.loadSearchedBooks(query, page)
        }
        currentQuery = query
    }

    fun loadMore() {
        clearData()
        page = page.inc()
        externalRepository.loadSearchedBooks(currentQuery, page)
    }
}