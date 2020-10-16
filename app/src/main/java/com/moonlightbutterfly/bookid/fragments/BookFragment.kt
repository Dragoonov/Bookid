package com.moonlightbutterfly.bookid.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.moonlightbutterfly.bookid.CustomItemDecoration
import com.moonlightbutterfly.bookid.R
import com.moonlightbutterfly.bookid.adapters.BookAdapterHorizontal
import com.moonlightbutterfly.bookid.databinding.BookFragmentBinding
import com.moonlightbutterfly.bookid.dialogs.AddBookToShelfDialog
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.utils.Converters
import com.moonlightbutterfly.bookid.utils.Layout
import com.moonlightbutterfly.bookid.utils.animatePulse
import com.moonlightbutterfly.bookid.viewmodels.BookViewModel
import kotlin.math.abs

class BookFragment : BaseFragment<BookFragmentBinding, BookViewModel>(BookViewModel::class.java) {

    private val args: BookFragmentArgs by navArgs()

    private val offsetChangedListener = AppBarLayout.OnOffsetChangedListener { layout: AppBarLayout, i: Int ->
        binding?.toolbar?.appTitle?.visibility =
            if (abs(i) >= layout.totalScrollRange) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
    }

    override fun inject() = appComponent.inject(this)

    override fun initializeViewModel() {
        super.initializeViewModel()
        val defaultShelfString = requireContext().getString(R.string.default_shelf)
        val bookAddedToDefaults = requireContext().getString(R.string.book_added, defaultShelfString)
        val favoriteName = requireContext().resources.getStringArray(R.array.basic_shelfs)[0]
        val bookAddedToFavourites = requireContext().getString(R.string.book_added, favoriteName)
        val bookRemovedFromFavourites = requireContext().getString(R.string.book_removed, favoriteName)
        val savedName = requireContext().resources.getStringArray(R.array.basic_shelfs)[1]
        val bookAddedToSaved = requireContext().getString(R.string.book_added, savedName)
        val bookRemovedFromSaved = requireContext().getString(R.string.book_removed, savedName)
        val errorOccurred = requireContext().getString(R.string.error_occurred)

        viewModel.apply {
            val bookString = args.book
            val book = Converters.convertToObject(bookString) as Book?
            setBook(book!!)
            bookAddedToDefaultsMessage = bookAddedToDefaults
            bookAddedToFavouritesMessage = bookAddedToFavourites
            bookRemovedFromFavouritesMessage = bookRemovedFromFavourites
            bookAddedToSavedMessage = bookAddedToSaved
            bookRemovedFromSavedMessage = bookRemovedFromSaved
            errorOccurredMessage = errorOccurred
        }
    }

    override fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = BookFragmentBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.authorBooks.listRecycler.apply {
                addItemDecoration(CustomItemDecoration(Layout.HORIZONTAL))
                layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = BookAdapterHorizontal()
            }
            it.similarBooks.listRecycler.apply {
                addItemDecoration(CustomItemDecoration(Layout.HORIZONTAL))
                layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = BookAdapterHorizontal()
            }
            it.addToShelf.apply {
                setOnClickListener { view ->
                    viewModel.insertBookToBaseShelf()
                    view.animatePulse()
                }
                setOnLongClickListener {
                    AddBookToShelfDialog
                        .newInstance(viewModel.bookLiveData.value!!)
                        .show((context as FragmentActivity).supportFragmentManager, AddBookToShelfDialog.NAME)
                    true
                }
            }
            it.appBar.addOnOffsetChangedListener(offsetChangedListener)
            it.favorite.setOnClickListener { view ->
                view.animatePulse()
                viewModel.handleFavoriteOperation()
            }
            it.saved.setOnClickListener { view ->
                view.animatePulse()
                viewModel.handleSavedOperation()
            }
            it.toolbar.share.setOnClickListener { view ->
                view.animatePulse()
                shareBook()
            }
        }
    }

    private fun shareBook() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, viewModel.bookLiveData.value?.title)
            type = "text/plain"
        }
        try {
            startActivity(sendIntent)
        } catch (ex: ActivityNotFoundException) {
            viewModel.onShareBookFail(getString(R.string.share_failed))
        }
    }
}
