package com.moonlightbutterfly.bookid.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moonlightbutterfly.bookid.fragments.BookFragment
import com.moonlightbutterfly.bookid.R
import com.moonlightbutterfly.bookid.databinding.BookContainerBinding
import com.moonlightbutterfly.bookid.databinding.RecyclerShelfLayoutBinding
import com.moonlightbutterfly.bookid.fragments.EditShelfsFragment
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf


class EditShelfsShelfAdapter(private val fragment: EditShelfsFragment) : RecyclerView.Adapter<EditShelfsShelfAdapter.ViewHolder>() {

    private val shelfs: MutableList<Shelf> = ArrayList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var binding: RecyclerShelfLayoutBinding? = null

        private lateinit var clicksListener: EditShelfsFragment

        constructor(binding: RecyclerShelfLayoutBinding, clicksListener: EditShelfsFragment) : this(binding.root) {
            this.binding = binding
            this.clicksListener = clicksListener
        }

        fun bind(shelf: Shelf) {
            binding?.shelf = shelf
            binding?.editShelf?.setOnClickListener {
                clicksListener.onShelfEditClick(shelf)
            }
            binding?.deleteShelf?.setOnClickListener {
                clicksListener.onShelfDeleteClick(shelf)
            }
            (binding?.booksRecycler?.adapter as EditShelfsBookAdapter).updateList(shelfs[adapterPosition])
            binding?.executePendingBindings()
        }
    }

    fun updateList(list: List<Shelf>?) {
        shelfs.clear()
        shelfs.addAll(list ?: ArrayList())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = RecyclerShelfLayoutBinding.inflate(layoutInflater, parent, false)
        itemBinding.booksRecycler.apply {
            adapter = EditShelfsBookAdapter(fragment)
            layoutManager = LinearLayoutManager(parent.context)
        }
        return ViewHolder(itemBinding, fragment)
    }

    override fun getItemCount(): Int = shelfs.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(shelfs[position])
    }
}