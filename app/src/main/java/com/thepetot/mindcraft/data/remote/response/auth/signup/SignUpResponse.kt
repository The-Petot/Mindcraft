package com.thepetot.mindcraft.data.remote.response.auth.signup

import com.google.gson.annotations.SerializedName

data class SignUpResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("links")
	val links: Links,

	@field:SerializedName("message")
	val message: String
)