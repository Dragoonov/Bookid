package com.moonlightbutterfly.bookid.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.moonlightbutterfly.bookid.R
import com.moonlightbutterfly.bookid.databinding.ShelfContainerBinding
import com.moonlightbutterfly.bookid.fragments.ShelfsListFragmentDirections
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.utils.DefaultShelf
import com.moonlightbutterfly.bookid.utils.provideLogoId
import com.moonlightbutterfly.bookid.viewmodels.ShelfsViewModel

class ShelfAdapter(private val viewModel: ShelfsViewModel) : RecyclerView.Adapter<ShelfAdapter.ViewHolder>() {

    val shelfs: MutableList<Shelf> = ArrayList()


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        constructor(binding: ShelfContainerBinding) : this(binding.root) {
            this.binding = binding
        }

        init {
            itemView.setOnClickListener {
                val action = ShelfsListFragmentDirections.actionShelfFragmentToBooksListFragment(shelfs[adapterPosition].id)
                it.findNavController().navigate(action)
            }
        }

        private lateinit var binding: ShelfContainerBinding

        fun bind(shelf: Shelf) = with(binding) {
            this.shelf = shelf
            this.defaultShelf = DefaultShelf.Companion
            this.edit.setOnClickListener {
                val action = ShelfsListFragmentDirections.actionShelfsToCreateEditShelfFragment(
                    it.context.getString(R.string.edit_shelf),
                    shelf.id
                )
                it.findNavController().navigate(action)
            }
            this.delete.setOnClickListener {
                shelfs.remove(shelf)
                viewModel.deleteShelf(shelf)
                notifyItemRemoved(adapterPosition)
            }
            this.base.setBackgroundColor(shelf.cover.background)
            this.front.setImageDrawable(
                ContextCompat.getDrawable(
                    this.front.context,
                    this.front.provideLogoId(shelf.cover.icon)
                )
            )
        }
    }

    fun updateList(list: List<Shelf>?) {
        shelfs.clear()
        shelfs.addAll(list ?: ArrayList())
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = shelfs.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(shelfs[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShelfAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ShelfContainerBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding)
    }
}