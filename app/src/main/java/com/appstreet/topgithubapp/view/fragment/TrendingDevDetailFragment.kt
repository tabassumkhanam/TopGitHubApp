package com.appstreet.topgithubapp.view.fragment

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.appstreet.topgithubapp.R
import com.appstreet.topgithubapp.databinding.FragmentTrendingDevDetailBinding
import com.appstreet.topgithubapp.imagecachelib.ImageHelper
import com.appstreet.topgithubapp.model.TrendingDeveloperModel
import com.appstreet.topgithubapp.view.activity.MainActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class TrendingDevDetailFragment : DaggerFragment() {

    private lateinit var fragmentBinding: FragmentTrendingDevDetailBinding
    private lateinit var developer: TrendingDeveloperModel
    @Inject
    lateinit var imageLibXCore: ImageHelper
    private var transitionName: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_trending_dev_detail, container, false)
        arguments?.let {
            if (it.containsKey(KEY_DEVELOPER_DETAIL)) {
                developer = it.getParcelable(KEY_DEVELOPER_DETAIL)!!
            }
            if (it.containsKey(KEY_TRANSITION_NAME)) {
                transitionName = it.getString(KEY_TRANSITION_NAME)
            }
        }
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentBinding.developer = developer
        fragmentBinding.ivAvatar.transitionName = transitionName
        developer.avatar?.let { imageLibXCore.loadBitmap(it, fragmentBinding.ivAvatar, R.mipmap.ic_launcher) }
    }

    companion object {

        private const val KEY_DEVELOPER_DETAIL = "developer_detail"
        private const val KEY_TRANSITION_NAME = "transition_name"

        fun create(
            trendingDeveloper: TrendingDeveloperModel,
            transitionName: String,
            activity: MainActivity
        ): TrendingDevDetailFragment {
            val developerDetailFragment = TrendingDevDetailFragment()
            developerDetailFragment.apply {
                arguments = Bundle()
                    .apply {
                        putString(KEY_TRANSITION_NAME, transitionName)
                        putParcelable(KEY_DEVELOPER_DETAIL, trendingDeveloper)
                    }
            }
            developerDetailFragment.sharedElementEnterTransition = TransitionInflater.from(activity)
                .inflateTransition(android.R.transition.move)
            return developerDetailFragment
        }
    }
}
