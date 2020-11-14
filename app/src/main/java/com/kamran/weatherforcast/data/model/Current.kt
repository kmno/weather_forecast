package com.kamran.weatherforcast.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Current(
    @SerializedName("dt")
    var dt: String,
    @SerializedName("temp")
    var temp: String,
    @SerializedName("feels_like")
    var feels_like: String,
    @SerializedName("weather")
    var weather: List<Weather>
)