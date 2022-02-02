package com.ronivaldoroner.concatadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

interface LoadMoreListeners {
    fun retry()
}

class LoadMoreStateAdapter(private val listeners: LoadMoreListeners) :
    ListAdapter<ScreenState, LoadMoreStateAdapter.StateViewHolder>(StateDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        StateViewHolder.from(parent, listeners)

    override fun onBindViewHolder(holder: StateViewHolder, position: Int) =
        holder.bind(getItem(position))

    class StateViewHolder private constructor(
        view: View,
        private val listeners: LoadMoreListeners
    ) : RecyclerView.ViewHolder(view) {
        fun bind(data: ScreenState) {
            with(itemView) {
                when (data) {
                    ScreenState.Loading -> {
                        findViewById<ProgressBar>(R.id.pbLoading).visibility = View.VISIBLE
                        findViewById<TextView>(R.id.tvError).visibility = View.GONE
                        findViewById<TextView>(R.id.btReload).visibility = View.GONE
                    }
                    ScreenState.Success -> {
                        findViewById<ProgressBar>(R.id.pbLoading).visibility = View.GONE
                        findViewById<TextView>(R.id.tvError).visibility = View.GONE
                        findViewById<TextView>(R.id.btReload).visibility = View.GONE
                    }
                    ScreenState.Error -> {
                        findViewById<ProgressBar>(R.id.pbLoading).visibility = View.GONE
                        findViewById<TextView>(R.id.tvError).visibility = View.VISIBLE
                        findViewById<TextView>(R.id.btReload).apply {
                            visibility = View.VISIBLE
                            setOnClickListener { listeners.retry() }
                        }
                    }
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup, listeners: LoadMoreListeners): StateViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(
                    R.layout.loading_layout,
                    parent,
                    false
                )
                return StateViewHolder(view, listeners)
            }
        }
    }

    private object StateDiff : DiffUtil.ItemCallback<ScreenState>() {
        override fun areItemsTheSame(oldItem: ScreenState, newItem: ScreenState) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: ScreenState, newItem: ScreenState) =
            oldItem == newItem

    }
}