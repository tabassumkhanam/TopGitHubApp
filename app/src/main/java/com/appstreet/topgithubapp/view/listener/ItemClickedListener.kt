package com.appstreet.topgithubapp.view.listener

import android.widget.ImageView
import com.appstreet.topgithubapp.model.TrendingDeveloperModel

interface ItemClickedListener {
    fun itemClicked(trendingDeveloper: TrendingDeveloperModel, imageView: ImageView)
}