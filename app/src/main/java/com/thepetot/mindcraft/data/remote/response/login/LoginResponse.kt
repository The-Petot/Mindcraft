package com.thepetot.mindcraft.data.remote.response.login

import com.google.gson.annotations.SerializedName
import com.thepetot.mindcraft.data.remote.response.signup.Data

data class LoginResponse(

    @field:SerializedName("data")
    val data: Data,

    @field:SerializedName("message")
    val message: String
)

data class Data(

    @field:SerializedName("userId")
    val userId: Int
)