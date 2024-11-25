package com.thepetot.mindcraft.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @SerializedName("data")
    val data: Data,

    @SerializedName("message")
    val message: String? = null,

    )

data class Data(

    @SerializedName("userId")
    val userId: Int,

)