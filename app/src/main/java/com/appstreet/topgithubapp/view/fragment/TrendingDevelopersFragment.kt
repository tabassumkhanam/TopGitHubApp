package com.appstreet.topgithubapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.appstreet.topgithub.utils.AppConstants.Companion.LANGUAGE
import com.appstreet.topgithub.utils.AppConstants.Companion.SINCE
import com.appstreet.topgithubapp.R
import com.appstreet.topgithubapp.imagecachelib.ImageHelper
import com.appstreet.topgithubapp.model.TrendingDeveloperModel
import com.appstreet.topgithubapp.networkcall.Resource
import com.appstreet.topgithubapp.view.activity.MainActivityNavController
import com.appstreet.topgithubapp.view.adapter.TrendingDevelopersListAdapter
import com.appstreet.topgithubapp.view.listener.ItemClickedListener
import com.appstreet.topgithubapp.view.viewmodel.TrendingDevViewModel
import com.appstreet.topgithubapp.view.viewmodel.ViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_trending_developers.*
import javax.inject.Inject

class TrendingDevelopersFragment : DaggerFragment(), ItemClickedListener {

    lateinit var viewModel: TrendingDevViewModel

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var navController: MainActivityNavController
    @Inject
    lateinit var imageLibXCore: ImageHelper

    private var developersList = ArrayList<TrendingDeveloperModel>()
    private lateinit var adapter: TrendingDevelopersListAdapter
    private var isAnimationNeeded = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TrendingDevViewModel::class.java)
        viewModel.callTrendingDevelopersRepositoryApi(LANGUAGE, SINCE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_trending_developers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerViewAdapter()
        viewModel.getTrendingDevelopersList().observe(viewLifecycleOwner, Observer { handleResult(it) })
    }

    /**
     * Set layout manager and adapter to recycler view
     */
    private fun setRecyclerViewAdapter() {
        val layoutManager = LinearLayoutManager(context)
        rvTrendingDevelopers.layoutManager = layoutManager
        adapter = TrendingDevelopersListAdapter(developersList, imageLibXCore, isAnimationNeeded, this)
        rvTrendingDevelopers.adapter = adapter
        isAnimationNeeded = false
    }

    private fun handleResult(result: Resource<List<TrendingDeveloperModel>>) {
        when (result) {
            is Resource.Loading -> {
                progressBar.visibility = View.VISIBLE
            }
            is Resource.Success -> {
                progressBar.visibility = View.GONE
                if (result.data != null) {
                    developersList.clear()
                    developersList.addAll(result.data)
                    adapter.notifyDataSetChanged()
                    if (developersList.isEmpty()){
                        tvNoDataFound.visibility = View.VISIBLE
                        tvNoDataFound.text = getString(R.string.no_developers_found)
                    }else{
                        tvNoDataFound.visibility = View.GONE
                    }
                }
            }
            is Resource.Error -> {
                progressBar.visibility = View.GONE
                tvNoDataFound.visibility = View.VISIBLE
                tvNoDataFound.text = result.message
            }
            is Resource.InternetError -> {
                progressBar.visibility = View.GONE
                tvNoDataFound.visibility = View.VISIBLE
                tvNoDataFound.text = getString(result.resId)
            }
        }
    }

    override fun itemClicked(trendingDeveloper: TrendingDeveloperModel, imageView: ImageView) {
        navController.navigateToDeveloperDetail(trendingDeveloper, imageView)
    }


    companion object {
        fun create() = TrendingDevelopersFragment()
    }
}