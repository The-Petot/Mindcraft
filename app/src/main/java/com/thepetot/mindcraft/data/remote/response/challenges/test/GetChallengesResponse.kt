package com.thepetot.mindcraft.data.remote.response.challenges.test

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class GetChallengesResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("links")
	val links: Links,

	@field:SerializedName("message")
	val message: String
) : Parcelable