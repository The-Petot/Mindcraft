package com.thepetot.mindcraft.data.remote.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(

	@field:SerializedName("error")
	val error: String
)
