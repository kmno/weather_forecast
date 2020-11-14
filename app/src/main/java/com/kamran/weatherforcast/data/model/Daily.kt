package com.kamran.weatherforcast.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Daily(
    @SerializedName("dt")
    var dt: String,
    @SerializedName("temp")
    var temp: Temp,
    @SerializedName("weather")
    var weather: List<Weather>
)