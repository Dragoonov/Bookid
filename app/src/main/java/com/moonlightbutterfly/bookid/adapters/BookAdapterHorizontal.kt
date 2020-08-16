package com.moonlightbutterfly.bookid.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.moonlightbutterfly.bookid.databinding.BookContainerHorizontalBinding
import com.moonlightbutterfly.bookid.repository.database.entities.Book

class BookAdapterHorizontal : BookAdapter() {

    inner class ViewHolderHorizontal(itemView: View) : ViewHolder(itemView) {

        constructor(binding: ViewDataBinding) : this(binding.root) {
            this.binding = binding
        }

        override fun bind(book: Book) = with(binding) {
            (this as BookContainerHorizontalBinding).book = book
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = BookContainerHorizontalBinding.inflate(layoutInflater, parent, false)
        return ViewHolderHorizontal(itemBinding)
    }
}