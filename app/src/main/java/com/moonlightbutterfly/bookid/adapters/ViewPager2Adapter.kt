package com.moonlightbutterfly.bookid.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.moonlightbutterfly.bookid.fragments.RecyclerFragment
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf

class ViewPager2Adapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private var shelfs: MutableList<Shelf> = ArrayList()

    fun replaceShelfs(newShelfs: List<Shelf>) {
        shelfs.clear()
        shelfs.addAll(newShelfs)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = shelfs.size

    override fun createFragment(position: Int): Fragment = RecyclerFragment.newInstance(shelfs[position].books)

}