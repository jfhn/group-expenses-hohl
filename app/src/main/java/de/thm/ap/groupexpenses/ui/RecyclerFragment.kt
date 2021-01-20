package de.thm.ap.groupexpenses.ui

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import de.thm.ap.groupexpenses.adapter.FirestoreAdapter

abstract class RecyclerFragment: Fragment() {
    var adapter: FirestoreAdapter<out RecyclerView.ViewHolder>? = null

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

    abstract fun initRecyclerView()
}