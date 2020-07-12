package com.moonlightbutterfly.bookid.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moonlightbutterfly.bookid.databinding.RecyclerBookLayoutBinding
import com.moonlightbutterfly.bookid.fragments.EditShelfsFragment
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf


class EditShelfsBookAdapter(private val fragment: EditShelfsFragment) : RecyclerView.Adapter<EditShelfsBookAdapter.ViewHolder>() {

    private val books: MutableList<Book> = ArrayList()
    private lateinit var shelf: Shelf

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var binding: RecyclerBookLayoutBinding? = null

        private lateinit var clicksListener: EditShelfsFragment

        constructor(binding: RecyclerBookLayoutBinding, clicksListener: EditShelfsFragment) : this(binding.root) {
            this.binding = binding
            this.clicksListener = clicksListener
        }

        fun bind(book: Book) = with(binding) {
            this?.book = book
            this?.deleteBook?.setOnClickListener {
                clicksListener.onBookDeleteClick(book, shelf)
            }
            this?.executePendingBindings()
        }
    }

    fun updateList(shelf: Shelf) {
        books.clear()
        books.addAll(shelf.books)
        this.shelf = shelf
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = RecyclerBookLayoutBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding, fragment)
    }

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(books[position])
    }
}