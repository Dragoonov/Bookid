package com.moonlightbutterfly.bookid.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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

        private fun changeVisibility(view: View) {
            val secondView =
                if (view.id == binding?.downArrow?.id) {
                    binding?.upArrow
                } else {
                    binding?.downArrow
                }
            view.visibility = View.INVISIBLE
            secondView?.visibility = View.VISIBLE
            if (view.id == R.id.downArrow) {
                binding?.booksRecycler?.visibility = View.VISIBLE
            } else {
                binding?.booksRecycler?.visibility = View.GONE
            }
        }

        fun bind(shelf: Shelf) {
            binding?.let {
                it.shelf = shelf
                it.editShelf.setOnClickListener {
                    clicksListener.onShelfEditClick(shelf)
                }
                it.deleteShelf.setOnClickListener {
                    clicksListener.onShelfDeleteClick(shelf)
                }
                it.title.setOnClickListener { _ -> changeVisibility(
                    if (it.upArrow.visibility == View.VISIBLE) {
                        it.upArrow
                    } else {
                        it.downArrow
                    })
                }
                it.upArrow.setOnClickListener { view -> changeVisibility(view) }
                it.downArrow.setOnClickListener { view -> changeVisibility(view) }
                (it.booksRecycler.adapter as EditShelfsBookAdapter).updateList(shelfs[adapterPosition])
                it.executePendingBindings()
            }

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