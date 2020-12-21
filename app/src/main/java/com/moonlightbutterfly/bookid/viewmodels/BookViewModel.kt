package com.moonlightbutterfly.bookid.viewmodels

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.*
import com.moonlightbutterfly.bookid.Communicator
import com.moonlightbutterfly.bookid.Manager
import com.moonlightbutterfly.bookid.SchedulerProvider
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.externalrepos.ExternalRepository
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import com.moonlightbutterfly.bookid.utils.DefaultShelf
import com.moonlightbutterfly.bookid.utils.removeDisplayedBookFromList
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BookViewModel @Inject constructor(
    private val repository: ExternalRepository,
    private val internalRepository: InternalRepository,
    private val userManager: Manager,
    private val schedulerProvider: SchedulerProvider,
    communicator: Communicator
) : BaseViewModel(internalRepository, communicator, schedulerProvider, userManager) {

    private var insertedToRecentlyViewed = false

    private var _bookLiveData = MutableLiveData<Book>()
    val bookLiveData: LiveData<Book> get() = _bookLiveData

    val authorBooksLiveData: LiveData<List<Book>?> = _bookLiveData.switchMap {
        LiveDataReactiveStreams.fromPublisher(
            repository
                .loadAuthorBooks(_bookLiveData.value?.authors?.get(0))
                .subscribeOn(schedulerProvider.io())
                .map {
                    it.removeDisplayedBookFromList(_bookLiveData.value as Book)
                }.toFlowable()
        )
    }

    val similarBooksLiveData: LiveData<List<Book>> = _bookLiveData.switchMap {
        LiveDataReactiveStreams.fromPublisher(
            repository
                .loadSimilarBooks(_bookLiveData.value?.authors?.get(0))
                .subscribeOn(schedulerProvider.io())
                .toFlowable()
        )
    }

    private val _isBookInFavoritesLiveData = favoriteShelfLiveData.map {
        isBookInFavorites(bookLiveData.value!!)
    }

    val isBookInFavoritesLiveData: LiveData<Boolean> get() = _isBookInFavoritesLiveData

    private val _isBookInSavedLiveData = savedShelfLiveData.map {
        isBookInSaved(bookLiveData.value!!)
    }

    val isBookInSavedLiveData: LiveData<Boolean> get() = _isBookInSavedLiveData

    val descriptionExpandedMode: LiveData<Boolean> get() = _descriptionExpandedMode
    private var _descriptionExpandedMode = MutableLiveData<Boolean>(false)


    fun setBook(book: Book) {
        if (_bookLiveData.value?.id == book.id) {
            return
        }
        this._bookLiveData.value = book
        insertBookToRecentlyViewed()
    }

    fun changeExpanded() {
        _descriptionExpandedMode.value = !_descriptionExpandedMode.value!!
    }

    fun openPurchaseLink(view: View) = openLink(view, bookLiveData.value?.accessInfo?.purchaseLink)

    fun openPreviewLink(view: View) = openLink(view, bookLiveData.value?.accessInfo?.previewLink)

    fun openInfoLink(view: View) = openLink(view, bookLiveData.value?.accessInfo?.infoLink)


    private fun openLink(view: View, url: String?) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(view.context.packageManager) != null) {
            view.context.startActivity(intent)
        }
    }

    fun handleFavoriteOperation() = handleFavoriteOperation(bookLiveData.value!!)

    fun handleSavedOperation() = handleSavedOperation(bookLiveData.value!!)

    private fun insertBookToRecentlyViewed() = disposable.add(internalRepository
        .getShelfByBaseId(DefaultShelf.RECENTLY_VIEWED.id, userManager.user.value!!.id)
        .subscribeOn(schedulerProvider.io())
        .subscribe {
            when {
                insertedToRecentlyViewed -> { }

                it!!.books.isEmpty() -> {
                    insertBookToShelf(bookLiveData.value, it)
                }
                it.books.contains(bookLiveData.value) -> {
                    if (it.books[0] != bookLiveData.value) {
                        deleteBookFromShelf(bookLiveData.value, it)
                        insertBookToShelf(bookLiveData.value, it, null, 0)
                    }
                }
                it.books.size >= 20 -> {
                    deleteBookFromShelf(19, it)
                    insertBookToShelf(bookLiveData.value, it, null, 0)
                }
                else -> insertBookToShelf(bookLiveData.value, it, null, 0)
            }
            insertedToRecentlyViewed = true
        })

    fun insertBookToBaseShelf() = insertBookToBaseShelf(bookLiveData.value!!)

    private fun deleteBookFromShelf(idx: Int?, shelf: Shelf?, message: String? = null) {
        if (shelf != null && idx != null && idx < shelf.books.size) {
            deleteBookFromShelf(shelf.books[idx], shelf, message)
        }
    }
}