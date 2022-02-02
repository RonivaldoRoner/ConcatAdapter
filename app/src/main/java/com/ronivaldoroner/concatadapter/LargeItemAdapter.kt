package com.ronivaldoroner.concatadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class LargeItemAdapter :
    ListAdapter<ItemViewObject, LargeItemAdapter.LargeItemsViewHolder>(LargeItemDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LargeItemsViewHolder.from(parent)


    override fun onBindViewHolder(holder: LargeItemsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class LargeItemsViewHolder private constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(data: ItemViewObject) {
            with(itemView) {
                findViewById<TextView>(R.id.tvTitle).text = data.title
                findViewById<TextView>(R.id.tvDescription).text = data.description
            }
        }

        companion object {
            fun from(parent: ViewGroup): LargeItemsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(
                    R.layout.large_item_layout,
                    parent,
                    false
                )
                return LargeItemsViewHolder(view)
            }
        }
    }

    private object LargeItemDiff : DiffUtil.ItemCallback<ItemViewObject>() {
        override fun areItemsTheSame(
            oldItemObject: ItemViewObject,
            newItemObject: ItemViewObject
        ) = oldItemObject == newItemObject

        override fun areContentsTheSame(
            oldItemObject: ItemViewObject,
            newItemObject: ItemViewObject
        ) = oldItemObject == newItemObject
    }
}