package com.kamran.weatherforcast.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Weather(
    @SerializedName("id")
    var id: String,
    @SerializedName("main")
    var main: String,
    @SerializedName("description")
    var description: String
)