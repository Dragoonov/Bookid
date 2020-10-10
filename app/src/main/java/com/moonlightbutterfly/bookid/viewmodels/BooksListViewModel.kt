package com.moonlightbutterfly.bookid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.moonlightbutterfly.bookid.Communicator
import com.moonlightbutterfly.bookid.Manager
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import javax.inject.Inject

class BooksListViewModel @Inject constructor(
    private val internalRepository: InternalRepository,
    private val userManager: Manager,
    communicator: Communicator
) : BaseViewModel(internalRepository, communicator, userManager) {

    private val customShelfIdLiveData = MutableLiveData(1000)
    val customShelfLiveData: LiveData<Shelf?> = customShelfIdLiveData.switchMap {
        LiveDataReactiveStreams.fromPublisher(
            internalRepository.getShelfById(it, userManager.user.value!!.id)
        )
    }

    fun setCustomShelfId(id: Int) = run { customShelfIdLiveData.value = id}

    fun deleteBookFromCustom(book: Book) = deleteBookFromShelf(book, customShelfLiveData.value)

}