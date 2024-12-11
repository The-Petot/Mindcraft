package com.thepetot.mindcraft.data.remote.response.user.update

import com.google.gson.annotations.SerializedName

data class UpdateUserResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("links")
	val links: Links,

	@field:SerializedName("message")
	val message: String
)