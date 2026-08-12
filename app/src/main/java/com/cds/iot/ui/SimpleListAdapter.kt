package com.cds.iot.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class SimpleListAdapter<T, VB : ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup, Boolean) -> VB,
    private val bind: (VB, T) -> Unit,
) : RecyclerView.Adapter<SimpleListAdapter.Holder<VB>>() {

    private val items = mutableListOf<T>()

    fun submit(data: List<T>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder<VB> {
        val binding = inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder<VB>, position: Int) {
        bind(holder.binding, items[position])
    }

    override fun getItemCount(): Int = items.size

    class Holder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}
