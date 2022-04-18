package com.sema.base.component

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * This class accepts databinding enabled layout files and uses
 * DiffUtil to update existing items for a RecyclerView
 */

typealias  OnItemClickListener<T> = (T) -> Unit

class CommonRecyclerAdapter<T>(
    @LayoutRes val layoutId: Int,
    val onBind: (ViewDataBinding, T, Int) -> Unit
) : RecyclerView.Adapter<CommonViewHolder>() {

    private val items: MutableList<T> = mutableListOf()
    var onItemClickListener: OnItemClickListener<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            layoutInflater, layoutId, parent, false
        )
        return CommonViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    fun isListEmpty(): Boolean {
        return items.isEmpty()
    }

    fun getItem(position: Int): T = items[position]

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        val item = items[position]
        onBind(holder.binding, item, position)
        holder.itemView.setOnClickListener { onItemClickListener?.invoke(item) }
    }

    fun updateItems(
        updatedItems: List<T>,
        itemPredicate: (T, T) -> Boolean,
        contentPredicate: (T, T) -> Boolean
    ) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                itemPredicate(items[oldItemPosition], updatedItems[newItemPosition])

            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = updatedItems.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                contentPredicate(items[oldItemPosition], updatedItems[newItemPosition])
        })

        items.clear()
        items += updatedItems

        diff.dispatchUpdatesTo(this)
    }

    fun resetItems(newItems: List<T>) {
        items.clear()
        items += newItems
        notifyDataSetChanged()
    }

    fun appendItems(newItems: List<T>) {
        val oldCount = itemCount
        items += newItems
        notifyItemRangeInserted(oldCount, newItems.size)
    }
}

class CommonViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)


