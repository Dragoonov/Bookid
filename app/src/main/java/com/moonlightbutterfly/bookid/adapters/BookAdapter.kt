package com.moonlightbutterfly.bookid.adapters

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.moonlightbutterfly.bookid.R
import com.moonlightbutterfly.bookid.fragments.BookFragmentDirections
import com.moonlightbutterfly.bookid.fragments.BooksListFragmentDirections
import com.moonlightbutterfly.bookid.fragments.SearchFragmentDirections
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.utils.Converters


abstract class BookAdapter : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    val books: MutableList<Book> = ArrayList()

    abstract inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                val navController = it.findNavController()
                val book = Converters.convertToJSONString(books[adapterPosition])!!
                when (navController.currentDestination?.id) {
                    R.id.search ->
                        navController.navigate(
                            SearchFragmentDirections
                                .actionSearchFragmentToBookFragment(book)
                        )
                    R.id.book ->
                        navController.navigate(
                            BookFragmentDirections
                                .actionBookFragmentSelf(book)
                        )
                    R.id.booksList ->
                        navController.navigate(
                            BooksListFragmentDirections
                                .actionBooksListFragmentToBookFragment(book)
                        )
                }

            }
        }

        protected var binding: ViewDataBinding? = null

        abstract fun bind(book: Book)
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

    fun removeAt(position: Int) {
        books.removeAt(position)
        notifyItemRemoved(position)
    }
}