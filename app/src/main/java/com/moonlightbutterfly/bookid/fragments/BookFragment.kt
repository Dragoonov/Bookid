package com.moonlightbutterfly.bookid.fragments

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnticipateOvershootInterpolator
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.moonlightbutterfly.bookid.Converters
import com.moonlightbutterfly.bookid.CustomItemDecoration
import com.moonlightbutterfly.bookid.R
import com.moonlightbutterfly.bookid.adapters.BookAdapterHorizontal
import com.moonlightbutterfly.bookid.adapters.LAYOUT
import com.moonlightbutterfly.bookid.databinding.BookFragmentBinding
import com.moonlightbutterfly.bookid.dialogs.AddBookToShelfDialog
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.viewmodels.BookViewModel
import kotlin.math.abs

class BookFragment : BaseFragment<BookFragmentBinding, BookViewModel>(BookViewModel::class.java) {

    private val args: BookFragmentArgs by navArgs()

    private val offsetChangedListener = AppBarLayout.OnOffsetChangedListener {layout: AppBarLayout, i: Int ->
        if (abs(i) >= layout.totalScrollRange) {
            binding?.toolbar?.appTitle?.visibility = View.VISIBLE
        } else {
            binding?.toolbar?.appTitle?.visibility = View.INVISIBLE
        }
    }


    override fun inject() = appComponent.inject(this)

    override fun initializeViewModel() {
        super.initializeViewModel()
        viewModel.let {
            val bookString = args.book
            val book = Converters.convertToObject(bookString) as Book?
            it.setBook(book!!)
        }
    }

    private fun handleAnimation(view: View) {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.5f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.5f)
        ObjectAnimator.ofPropertyValuesHolder(view,scaleX,scaleY).apply {
            repeatCount = 1
            repeatMode = ValueAnimator.REVERSE
            duration = 200
            start()
        }
    }

    override fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = BookFragmentBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.authorBooks.listRecycler.apply {
                addItemDecoration(CustomItemDecoration(LAYOUT.HORIZONTAL))
                layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = BookAdapterHorizontal()
            }
            it.similarBooks.listRecycler.apply {
                addItemDecoration(CustomItemDecoration(LAYOUT.HORIZONTAL))
                layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = BookAdapterHorizontal()
            }
            it.addToShelf.apply {
                setOnClickListener { view ->
                    val defaultShelfString = view.context.getString(R.string.default_shelf)
                    viewModel.insertBookToBaseShelf(view.context.getString(R.string.book_added, defaultShelfString))
                    handleAnimation(view)
                }
                setOnLongClickListener {
                    AddBookToShelfDialog
                        .newInstance(viewModel.bookLiveData.value!!)
                        .show((context as FragmentActivity).supportFragmentManager, AddBookToShelfDialog.NAME)
                    true
                }
                it.appBar.addOnOffsetChangedListener(offsetChangedListener)
            }
            it.favorite.apply {
                setOnClickListener {view ->
                    handleAnimation(view)
                    val favoriteName = view.context.resources.getStringArray(R.array.basic_shelfs)[0]
                    viewModel.handleFavoriteOperation(
                        view.context.getString(R.string.book_added, favoriteName),
                        view.context.getString(R.string.book_removed, favoriteName))
                }
            }
            it.saved.apply {
                setOnClickListener {view ->
                    handleAnimation(view)
                    val savedName = view.context.resources.getStringArray(R.array.basic_shelfs)[1]
                    viewModel.handleSavedOperation(
                        view.context.getString(R.string.book_added, savedName),
                        view.context.getString(R.string.book_removed, savedName))
                }
            }
            it.toolbar.share.apply {
                setOnClickListener {view ->
                    handleAnimation(view)
                }
            }
        }
    }

}
