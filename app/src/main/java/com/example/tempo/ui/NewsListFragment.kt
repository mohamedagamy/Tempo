package com.example.tempo.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tempo.R
import com.example.tempo.data.model.Status
import com.example.tempo.databinding.FragmentNewsListBinding
import com.example.tempo.paging.EndlessRecyclerViewScrollListener
import com.example.tempo.showToast
import com.example.tempo.ui.adapter.NewsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
@AndroidEntryPoint
class NewsListFragment : Fragment() {
    private var _binding: FragmentNewsListBinding? = null
    private val binding get() = _binding
    lateinit var newsAdapter:NewsListAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var scrollListener: EndlessRecyclerViewScrollListener
    var searchKey:String = ""
    var pageNum:Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNewsListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        setUpAdapter()
        initSearchView()
        initSwipeRefresh()
    }

    @SuppressLint("CheckResult")
    private fun getNewsList(pageNum: Int) {
        binding?.swipeToRefresh?.isRefreshing = true
        (activity as MainActivity).appViewModel.getNewsList(searchKey,pageNum)
            .observe(viewLifecycleOwner,{
                binding?.swipeToRefresh?.isRefreshing = false
                when(it.status){
                    Status.SUCCESS -> {
                        context?.showToast("Success")
                    }
                    Status.ERROR -> {
                        context?.showToast("Error")
                    }
                    Status.LOADING -> {
                        binding?.swipeToRefresh?.isRefreshing = true
                    }
                }

        })
    }

    private fun initSearchView() {
        val searchview = binding?.simpleSearchView
        searchview?.setOnQueryTextListener(object :SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val isSearchOk = !searchKey.equals(query.toString())
                if(isSearchOk) {
                    scrollListener.resetState()
                    pageNum = 1
                    searchKey = query.toString()
                    newsAdapter.clearNews()
                    getNewsList(pageNum)
                }else{
                    activity?.showToast("please enter new search key")
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun initSwipeRefresh() {
        binding?.swipeToRefresh?.setOnRefreshListener {
            Handler().postDelayed(Runnable { // Stop animation (This will be after 3 seconds)
                binding?.swipeToRefresh?.isRefreshing = false
            }, 200) // Delay in millis
        }
    }

    private fun setUpAdapter() {
        val rvNewsList: RecyclerView? = binding?.rvNewsList
        layoutManager = LinearLayoutManager(context)
        newsAdapter = NewsListAdapter((activity as MainActivity).glide)
        rvNewsList?.layoutManager =  layoutManager
        rvNewsList?.adapter = newsAdapter

        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadMoreData()
                Timber.d("pageNumber: ${page} itemCount: ${totalItemsCount} adapter size: ${newsAdapter.itemCount}")
            }

        }
        rvNewsList?.addOnScrollListener(scrollListener)

        val itemDetailFragmentContainer: View? = view?.findViewById(R.id.item_detail_nav_container)
        //handle item click
        newsAdapter.clickListener = { itemView, item ->
            val bundle = bundleOf(NewsDetailFragment.ARG_ITEM_ID to (activity as MainActivity).gson.toJson(item))
            if(itemDetailFragmentContainer != null) {
                itemDetailFragmentContainer.findNavController().navigate(R.id.fragment_item_detail, bundle)
            }else{
                itemView.findNavController().navigate(R.id.show_item_detail, bundle)
            }
        }

        (activity as MainActivity).appViewModel.newsList.observe(viewLifecycleOwner,{
            newsAdapter.appendNews(it.toMutableList())
        })
    }

    private fun loadMoreData() {
        pageNum++
        if(pageNum < 6)
            getNewsList(pageNum)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}