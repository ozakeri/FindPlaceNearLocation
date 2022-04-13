package com.example.findplacenearlocation.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.findplacenearlocation.R
import com.example.findplacenearlocation.adapter.VenueListAdapter
import com.example.findplacenearlocation.ui.MainActivity
import com.example.findplacenearlocation.ui.viewmodel.VenueViewModel
import com.example.findplacenearlocation.utils.Constant.LIMIT
import com.example.findplacenearlocation.utils.Constant.QUERY_PAGE_SIZE
import com.example.findplacenearlocation.utils.Resource
import kotlinx.android.synthetic.main.item_error_message.*
import kotlinx.android.synthetic.main.list_fragment.*


class ListFragment : Fragment(R.layout.list_fragment) {

    lateinit var viewModel: VenueViewModel
    lateinit var venueListAdapte: VenueListAdapter
    val lng = "35.7630102,51.4425469"
    val totalResults: Int = 100

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()


        venueListAdapte.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("venue", it)
            }
            findNavController().navigate(
                R.id.detailFragment,
                bundle
            )
        }

        viewModel.venueList.observe(viewLifecycleOwner, Observer {response ->
            when(response){
                is Resource.Success->{
                    hideErrorMessage()
                    hideProgressBar()
                    response.data?.let {dataValue->
                        venueListAdapte.differ.submitList(dataValue.response.venues.toList())
                        val totalPages = totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.pageNumber == totalPages
                        if(isLastPage) {
                            recyclerview.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG).show()
                        showErrorMessage(message)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        viewModel.getVenueList(lng)

        btnRetry.setOnClickListener {
            viewModel.getVenueList(lng)
        }
    }

    private fun setupRecyclerView() {
        venueListAdapte = VenueListAdapter()
        recyclerview.apply {
            adapter = venueListAdapte
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@ListFragment.scrollListener)
        }
    }


    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideErrorMessage() {
        itemErrorMessage.visibility = View.INVISIBLE
        isError = false
    }

    private fun showErrorMessage(message: String) {
        itemErrorMessage.visibility = View.VISIBLE
        tvErrorMessage.text = message
        isError = true
    }

    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                viewModel.getVenueList(lng)
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }
}