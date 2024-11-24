package com.thepetot.mindcraft.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

    @field:SerializedName("data")
    val data: Data,

    @field:SerializedName("message")
    val message: String? = null,

)