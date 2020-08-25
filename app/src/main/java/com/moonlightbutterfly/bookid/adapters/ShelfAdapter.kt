package com.moonlightbutterfly.bookid.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.moonlightbutterfly.bookid.databinding.ShelfContainerBinding
import com.moonlightbutterfly.bookid.dialogs.RenameShelfDialog
import com.moonlightbutterfly.bookid.fragments.ShelfFragmentDirections
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.utils.BasicShelfsId
import com.moonlightbutterfly.bookid.viewmodels.ShelfViewModel

class ShelfAdapter(private val viewModel: ShelfViewModel) : RecyclerView.Adapter<ShelfAdapter.ViewHolder>() {

    val shelfs: MutableList<Shelf> = ArrayList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        constructor(binding: ShelfContainerBinding) : this(binding.root) {
            this.binding = binding
        }

        init {
            itemView.setOnClickListener {
                val action = ShelfFragmentDirections.actionShelfFragmentToBooksListFragment(shelfs[adapterPosition].id)
                it.findNavController().navigate(action)
            }
        }

        private var binding: ShelfContainerBinding? = null

        fun bind(shelf: Shelf) = with(binding) {
            this?.shelf = shelf
            this?.viewModel = viewModel
            this?.basicShelfsId = BasicShelfsId.Companion
            this?.edit?.setOnClickListener {
                RenameShelfDialog.newInstance(Shelf(shelf.id,shelf.name,shelf.books,shelf.userId))
                    .show((it.context as AppCompatActivity).supportFragmentManager, RenameShelfDialog.NAME)
            }
            this?.delete?.setOnClickListener {
                shelfs.remove(shelf)
                viewModel?.deleteShelf(shelf)
                notifyItemRemoved(adapterPosition)
            }
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