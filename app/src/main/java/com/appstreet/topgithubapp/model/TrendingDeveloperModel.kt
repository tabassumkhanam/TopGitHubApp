package com.appstreet.topgithubapp.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TrendingDeveloperModel(
    @SerializedName("username") @Expose var username: String?,
    @SerializedName("name") @Expose var name: String?,
    @SerializedName("type") @Expose var type: String?,
    @SerializedName("url") @Expose var url: String?,
    @SerializedName("avatar") @Expose var avatar: String?,
    @SerializedName("repo") @Expose var repo: RepoModel?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        TODO("repo")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(name)
        parcel.writeString(type)
        parcel.writeString(url)
        parcel.writeString(avatar)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TrendingDeveloperModel> {
        override fun createFromParcel(parcel: Parcel): TrendingDeveloperModel {
            return TrendingDeveloperModel(parcel)
        }

        override fun newArray(size: Int): Array<TrendingDeveloperModel?> {
            return arrayOfNulls(size)
        }
    }
}
