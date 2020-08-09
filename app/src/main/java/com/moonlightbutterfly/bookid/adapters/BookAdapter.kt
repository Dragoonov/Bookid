package com.moonlightbutterfly.bookid.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.moonlightbutterfly.bookid.AppGraphDirections
import com.moonlightbutterfly.bookid.Converters
import com.moonlightbutterfly.bookid.databinding.BookContainerBinding
import com.moonlightbutterfly.bookid.databinding.BookContainerHorizontalBinding
import com.moonlightbutterfly.bookid.repository.database.entities.Book


class BookAdapter(private val layoutType: LAYOUT = LAYOUT.HORIZONTAL) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    val books: MutableList<Book> = ArrayList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                val action = AppGraphDirections.actionGlobalBookFragment(Converters.convertToJSONString(books[adapterPosition])!!)
                    it.findNavController().navigate(action)
            }
        }

        private var binding: ViewDataBinding? = null

        constructor(binding: ViewDataBinding) : this(binding.root) {
            this.binding = binding
        }

        fun bind(book:Book) = with(binding) {
            if (this is BookContainerBinding) {
                this.book = book
            } else if (this is BookContainerHorizontalBinding) {
                this.book = book
            }
            this?.executePendingBindings()
        }

    }

    fun updateList(list: List<Book>?) {
        books.addAll(list ?: ArrayList())
        notifyDataSetChanged()
    }

    fun clearList() {
        books.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = if (layoutType == LAYOUT.VERTICAL) {
            BookContainerBinding.inflate(layoutInflater, parent, false)
        } else {
            BookContainerHorizontalBinding.inflate(layoutInflater, parent, false)
        }
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(books[position])
    }
}

enum class LAYOUT {
    HORIZONTAL, VERTICAL
}