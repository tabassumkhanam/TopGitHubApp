package com.appstreet.topgithubapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.animation.ScaleAnimation
import android.view.animation.Animation
import androidx.databinding.DataBindingUtil
import com.appstreet.topgithubapp.BR
import com.appstreet.topgithubapp.R
import com.appstreet.topgithubapp.databinding.ItemTrendingDeveloperBinding
import com.appstreet.topgithubapp.imagecachelib.ImageHelper
import com.appstreet.topgithubapp.model.TrendingDeveloperModel
import com.appstreet.topgithubapp.view.listener.ItemClickedListener


class TrendingDevelopersListAdapter(
    private val developersList: List<TrendingDeveloperModel>,
    private val imageLibXCore: ImageHelper,
    private var isAnimationNeeded: Boolean,
    private val listener: ItemClickedListener
) : RecyclerView.Adapter<TrendingDevelopersListAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemRowBinding: ItemTrendingDeveloperBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_trending_developer, parent, false)
        return MyViewHolder(itemRowBinding)
    }

    override fun getItemCount(): Int {
        return developersList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(developersList.get(position))

        if (isAnimationNeeded) setScaleAnimation(holder.itemView, position)
        if (position == developersList.size - 1) isAnimationNeeded = false
    }

    inner class MyViewHolder(var itemRowBinding: ItemTrendingDeveloperBinding) :
        RecyclerView.ViewHolder(itemRowBinding.getRoot()) {

        fun bind(trendingDeveloper: TrendingDeveloperModel) {
            itemRowBinding.setVariable(BR.position, adapterPosition)
            itemRowBinding.setVariable(BR.developer, trendingDeveloper)
            itemRowBinding.setVariable(BR.itemClickListener, listener)
            itemRowBinding.executePendingBindings()
            developersList[adapterPosition].avatar?.let {
                imageLibXCore.loadBitmap(
                    it,
                    itemRowBinding.ivAvatar,
                    R.mipmap.ic_launcher
                )
            }
        }
    }


    private fun setScaleAnimation(view: View, position: Int) {
            val anim = ScaleAnimation(
                0.0f,
                1.0f,
                0.0f,
                1.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            anim.duration = 600
            view.startAnimation(anim)
        }
}