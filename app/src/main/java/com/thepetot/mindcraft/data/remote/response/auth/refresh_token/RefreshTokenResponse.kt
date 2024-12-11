package com.thepetot.mindcraft.data.remote.response.auth.refresh_token

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("links")
	val links: Links,

	@field:SerializedName("message")
	val message: String
)