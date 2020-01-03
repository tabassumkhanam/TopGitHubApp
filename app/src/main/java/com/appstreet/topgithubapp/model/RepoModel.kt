package com.appstreet.topgithubapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RepoModel(
    @SerializedName("name") @Expose var name: String?,
    @SerializedName("description") @Expose var description: String?,
    @SerializedName("url") @Expose var url: String?
)
