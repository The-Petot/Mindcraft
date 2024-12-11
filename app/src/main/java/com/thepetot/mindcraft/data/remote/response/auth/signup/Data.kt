package com.thepetot.mindcraft.data.remote.response.auth.signup

import com.google.gson.annotations.SerializedName

data class Data(

	@field:SerializedName("userId")
	val userId: Int
)