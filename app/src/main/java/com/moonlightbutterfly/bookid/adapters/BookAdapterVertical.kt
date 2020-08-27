package com.moonlightbutterfly.bookid.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.moonlightbutterfly.bookid.R
import com.moonlightbutterfly.bookid.databinding.BookContainerVerticalBinding
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.viewmodels.BooksListViewModel

class BookAdapterVertical(private val showIcons: Boolean = false,
                          private val booksListViewModel: BooksListViewModel? = null) :
    BookAdapter() {

    inner class ViewHolderVertical(itemView: View) : ViewHolder(itemView) {

        private lateinit var favoriteEmptyDrawable: Drawable
        private lateinit var favoriteFilledDrawable: Drawable
        private lateinit var savedEmptyDrawable: Drawable
        private lateinit var savedFilledDrawable: Drawable

        constructor(binding: ViewDataBinding) : this(binding.root) {
            this.binding = binding
            favoriteEmptyDrawable = ContextCompat.getDrawable(binding.root.context, R.drawable.ic_favorite_border_24px)!!
            favoriteFilledDrawable = ContextCompat.getDrawable(binding.root.context, R.drawable.ic_favorite_24px)!!
            savedEmptyDrawable = ContextCompat.getDrawable(binding.root.context, R.drawable.ic_bookmark_border_24px)!!
            savedFilledDrawable = ContextCompat.getDrawable(binding.root.context, R.drawable.ic_bookmark_24px)!!
        }

        override fun bind(book: Book) = with(binding) {
            (this as BookContainerVerticalBinding).apply {
                this.book = book
                if (showIcons) {
                    add.setOnClickListener {
                        booksListViewModel?.insertBookToBaseShelf(book)
                    }
                    setupFavorites(book)
                    setupSaved(book)
                } else {
                    add.visibility = View.GONE
                    favorite.visibility = View.GONE
                    saved.visibility = View.GONE
                }
            }
            executePendingBindings()
        }

        private fun setupFavorites(book: Book) {
            (binding as BookContainerVerticalBinding).favorite.let { view ->
                view.setImageDrawable(
                    if (booksListViewModel?.isBookInFavorites(book)!!) {
                        favoriteFilledDrawable
                    } else {
                        favoriteEmptyDrawable
                    }
                )
                view.setOnClickListener {
                    booksListViewModel.handleFavoriteOperation(book)
                    (it as ImageView).setImageDrawable(
                        if (it.drawable == favoriteEmptyDrawable) {
                            favoriteFilledDrawable
                        } else {
                            favoriteEmptyDrawable
                        }
                    )
                }
            }
        }

        private fun setupSaved(book: Book) {
            (binding as BookContainerVerticalBinding).saved.let { view ->
                view.setImageDrawable(
                    if (booksListViewModel?.isBookInSaved(book)!!) {
                        savedFilledDrawable
                    } else {
                        savedEmptyDrawable
                    }
                )
                view.setOnClickListener {
                    booksListViewModel.handleSavedOperation(book)
                    (it as ImageView).setImageDrawable(
                        if (it.drawable == savedFilledDrawable) {
                            savedEmptyDrawable
                        } else {
                            savedFilledDrawable
                        }
                    )
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = BookContainerVerticalBinding.inflate(layoutInflater, parent, false)
        return ViewHolderVertical(itemBinding)
    }
}