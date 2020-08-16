package com.moonlightbutterfly.bookid.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.moonlightbutterfly.bookid.databinding.BookContainerVerticalBinding
import com.moonlightbutterfly.bookid.repository.database.entities.Book

class BookAdapterVertical : BookAdapter() {

    inner class ViewHolderVertical(itemView: View) : ViewHolder(itemView) {

        constructor(binding: ViewDataBinding) : this(binding.root) {
            this.binding = binding
        }

        override fun bind(book: Book) = with(binding) {
            (this as BookContainerVerticalBinding).book = book
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = BookContainerVerticalBinding.inflate(layoutInflater, parent, false)
        return ViewHolderVertical(itemBinding)
    }
}