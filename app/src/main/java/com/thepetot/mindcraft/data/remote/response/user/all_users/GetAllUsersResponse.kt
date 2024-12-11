package com.thepetot.mindcraft.data.remote.response.user.all_users

import com.google.gson.annotations.SerializedName

data class GetAllUsersResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("links")
	val links: Links,

	@field:SerializedName("message")
	val message: String
)