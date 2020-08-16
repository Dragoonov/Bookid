package com.moonlightbutterfly.bookid.adapters

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.moonlightbutterfly.bookid.AppGraphDirections
import com.moonlightbutterfly.bookid.Converters
import com.moonlightbutterfly.bookid.repository.database.entities.Book


abstract class BookAdapter : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    val books: MutableList<Book> = ArrayList()

    abstract inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                val action = AppGraphDirections.actionGlobalBookFragment(Converters.convertToJSONString(books[adapterPosition])!!)
                    it.findNavController().navigate(action)
                //TODO: Zmienic jak zmienie Navigation
            }
        }

        protected var binding: ViewDataBinding? = null

        abstract fun bind(book:Book)
    }

    fun updateList(list: List<Book>?) {
        books.addAll(list ?: ArrayList())
        notifyDataSetChanged()
    }

    fun clearList() {
        books.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(books[position])
    }
}

enum class LAYOUT {
    HORIZONTAL, VERTICAL
}