/*
 * Creator: Kamran Noorinejad on 5/19/20 12:11 PM
 * Last modified: 5/19/20 12:11 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */
package com.kamran.weatherforcast.data.api

import androidx.annotation.Keep
import com.kamran.weatherforcast.data.model.Forecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Kamran Noorinejad on 5/19/2020 AD 12:11.
 * Edited by Kamran Noorinejad on 5/19/2020 AD 12:11.
 */
@Keep
interface ApiService {
    //One call
    @GET("/data/2.5/onecall")
    suspend fun getWeatherForecast(
        @Query("lat", encoded = true) lat: String,
        @Query("lon", encoded = true) lon: String,
        @Query("units", encoded = true) units: String,
        @Query("appid", encoded = true) appid: String
    ): Response<Forecast>
}