package com.ronivaldoroner.concatadapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity(), LoadMoreListeners {

    private val largeItemAdapter: LargeItemAdapter by lazy { LargeItemAdapter() }
    private val smallItemAdapter: SmallItemAdapter by lazy { SmallItemAdapter() }
    private val loadMoreStateAdapter: LoadMoreStateAdapter by lazy {
        LoadMoreStateAdapter(this)
    }
    private val concatAdapter: ConcatAdapter by lazy {
        ConcatAdapter(
            largeItemAdapter,
            smallItemAdapter
        )
    }
    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        initListeners()
        initValues()
    }

    private fun setupRecyclerView() {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.SPACE_BETWEEN
        rvConcatAdapter.layoutManager = layoutManager
        rvConcatAdapter.adapter = concatAdapter
    }

    private fun initListeners() {
        rvConcatAdapter.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {

                    if (concatAdapter.adapters.find { it == loadMoreStateAdapter } == null) {
                        concatAdapter.addAdapter(loadMoreStateAdapter)
                    }

                    postLoadMoreState(ScreenState.Loading)
                    loadMoreSmallItem(false)
                }
            }
        })
    }

    private fun initValues() {
        mockLargeItems()
        mockSmallItems()
    }

    private fun postLoadMoreState(newState: ScreenState) {
        loadMoreStateAdapter.submitList(listOf(newState))
    }

    private fun loadMoreSmallItem(isSuccess: Boolean) {
        Timer("Daley", false).schedule(500) {
            if (isSuccess) {
                page++
                mockSmallItems()
            } else {
                postLoadMoreState(ScreenState.Error)
            }
        }
    }

    override fun retry() {
        postLoadMoreState(ScreenState.Loading)
        loadMoreSmallItem(true)
    }

    private fun mockLargeItems() {
        val listItems = mutableListOf<ItemViewObject>()
        repeat(5) {
            listItems.add(
                ItemViewObject(
                    id = it,
                    title = "Large Item: $it",
                    description = "Descrição do Item: $it"
                )
            )
        }

        largeItemAdapter.submitList(listItems)
    }

    private fun mockSmallItems() {
        val pagination = page * 100 - 100
        val listItems = mutableListOf<ItemViewObject>()

        repeat(100) {
            val count = it + pagination
            listItems.add(
                ItemViewObject(
                    id = count,
                    title = "Small Item: $count",
                    description = "Descrição do Item: $count, pagina: $page"
                )
            )
        }

        smallItemAdapter.submitList(listItems){
            postLoadMoreState(ScreenState.Success)
        }
    }
}