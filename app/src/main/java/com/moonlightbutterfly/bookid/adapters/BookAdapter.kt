package com.moonlightbutterfly.bookid.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.moonlightbutterfly.bookid.fragments.BookFragment
import com.moonlightbutterfly.bookid.R
import com.moonlightbutterfly.bookid.Utils
import com.moonlightbutterfly.bookid.databinding.BookContainerBinding
import com.moonlightbutterfly.bookid.repository.database.entities.Book


class BookAdapter : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    val books: MutableList<Book> = ArrayList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                (it.context as AppCompatActivity).supportFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        BookFragment.newInstance(Utils.convertToJSONString(books[adapterPosition])))
                    .addToBackStack(null)
                    .commit()
            }
        }

        private var binding: BookContainerBinding? = null

        constructor(binding: BookContainerBinding) : this(binding.root) {
            this.binding = binding
        }

        fun bind(book:Book) {
            binding?.book = book
            binding?.executePendingBindings()
        }
    }

    fun updateList(list: List<Book>?) {
        books.addAll(list ?: ArrayList())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = BookContainerBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(books[position])
    }
}