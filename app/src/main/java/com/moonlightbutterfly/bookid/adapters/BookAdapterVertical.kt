package com.moonlightbutterfly.bookid.adapters

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import com.moonlightbutterfly.bookid.R
import com.moonlightbutterfly.bookid.databinding.BookContainerVerticalBinding
import com.moonlightbutterfly.bookid.dialogs.AddBookToShelfDialog
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.utils.animatePulse
import com.moonlightbutterfly.bookid.viewmodels.BaseViewModel

class BookAdapterVertical(
    private val showIcons: Boolean = false,
    private val booksListViewModel: BaseViewModel? = null
) :
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

        override fun bind(book: Book) = with(binding as BookContainerVerticalBinding) {
            this.book = book
            if (showIcons) {
                setupAdd()
                setupShare()
                setupFavorites(book)
                setupSaved(book)
            } else {
                add.visibility = View.GONE
                favorite.visibility = View.GONE
                saved.visibility = View.GONE
            }
            executePendingBindings()
        }

        private fun setupAdd() = (binding as BookContainerVerticalBinding).add.apply {
            val binding = binding as BookContainerVerticalBinding
            setOnClickListener {
                booksListViewModel?.insertBookToBaseShelf(binding.book)
                it.animatePulse()
            }
            setOnLongClickListener {
                AddBookToShelfDialog
                    .newInstance(binding.book!!)
                    .show((context as FragmentActivity).supportFragmentManager, AddBookToShelfDialog.NAME)
                true
            }
        }

        private fun setupShare() = (binding as BookContainerVerticalBinding).share.apply {
            setOnClickListener {
                it.animatePulse()
                shareBook(it.context, (binding as BookContainerVerticalBinding).book?.title!!)
            }
        }

        private fun setupFavorites(book: Book) =
            (binding as BookContainerVerticalBinding).favorite.apply {
                setImageDrawable(
                    if (booksListViewModel?.isBookInFavorites(book)!!) {
                        favoriteFilledDrawable
                    } else {
                        favoriteEmptyDrawable
                    }
                )
                setOnClickListener {
                    booksListViewModel.handleFavoriteOperation(book)
                    (it as ImageView).setImageDrawable(
                        if (it.drawable == favoriteEmptyDrawable) {
                            favoriteFilledDrawable
                        } else {
                            favoriteEmptyDrawable
                        }
                    )
                    animatePulse()
                }
            }

        private fun setupSaved(book: Book) =
            (binding as BookContainerVerticalBinding).saved.apply {
                setImageDrawable(
                    if (booksListViewModel?.isBookInSaved(book)!!) {
                        savedFilledDrawable
                    } else {
                        savedEmptyDrawable
                    }
                )
                setOnClickListener {
                    booksListViewModel.handleSavedOperation(book)
                    (it as ImageView).setImageDrawable(
                        if (it.drawable == savedFilledDrawable) {
                            savedEmptyDrawable
                        } else {
                            savedFilledDrawable
                        }
                    )
                    animatePulse()
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = BookContainerVerticalBinding.inflate(layoutInflater, parent, false)
        return ViewHolderVertical(itemBinding)
    }

    private fun shareBook(context: Context, title: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, title)
            type = "text/plain"
        }
        try {
            context.startActivity(sendIntent)
        } catch (ex: ActivityNotFoundException) {
            booksListViewModel?.onShareBookFail(context.getString(R.string.share_failed))
        }
    }
}