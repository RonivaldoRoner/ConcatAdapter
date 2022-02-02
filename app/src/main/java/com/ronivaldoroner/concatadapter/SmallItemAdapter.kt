package com.ronivaldoroner.concatadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class SmallItemAdapter :
    ListAdapter<ItemViewObject, SmallItemAdapter.SmallItemsViewHolder>(LargeItemDiff) {

    private var initialList: List<ItemViewObject> = emptyList()

    override fun submitList(list: MutableList<ItemViewObject>?, commitCallback: Runnable?) {
        initialList = initialList.copyWith(list!!)
        super.submitList(initialList, commitCallback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SmallItemsViewHolder.from(parent)


    override fun onBindViewHolder(holder: SmallItemsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SmallItemsViewHolder private constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(data: ItemViewObject) {
            with(itemView) {
                findViewById<TextView>(R.id.tvTitle).text = data.title
                findViewById<TextView>(R.id.tvDescription).text = data.description
            }
        }

        companion object {
            fun from(parent: ViewGroup): SmallItemsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(
                    R.layout.small_item_layout,
                    parent,
                    false
                )
                return SmallItemsViewHolder(view)
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

inline fun <reified T> List<T>.copyWith(anotherList: List<T>): List<T> = listOf(
    *this.toTypedArray(),
    *anotherList.toTypedArray()
).distinct()