package com.moonlightbutterfly.bookid.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.moonlightbutterfly.bookid.AppGraphDirections
import com.moonlightbutterfly.bookid.Converters
import com.moonlightbutterfly.bookid.databinding.BookContainerBinding
import com.moonlightbutterfly.bookid.repository.database.entities.Book


class BookAdapter(private val layoutType: LAYOUT = LAYOUT.HORIZONTAL) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    val books: MutableList<Book> = ArrayList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                val action = AppGraphDirections.actionGlobalBookFragment(Converters.convertToJSONString(books[adapterPosition]))
                    it.findNavController().navigate(action)
            }
        }

        private var binding: BookContainerBinding? = null

        constructor(binding: BookContainerBinding, layoutType: LAYOUT) : this(binding.root) {
            this.binding = binding.also {
                it.layoutType = layoutType
            }
        }

        fun bind(book:Book) {
            with(binding) {
                this?.book = book
                this?.executePendingBindings()
            }
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
        val itemBinding = BookContainerBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding, layoutType)
    }

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(books[position])
    }
}

enum class LAYOUT {
    HORIZONTAL, VERTICAL
}