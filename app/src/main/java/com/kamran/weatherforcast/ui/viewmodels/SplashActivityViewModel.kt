/*
 * Creator: Kamran Noorinejad on 5/13/20 12:55 PM
 * Last modified: 5/13/20 12:55 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kamran.weatherforcast.ui.viewmodels

import android.content.Context
import android.util.Config
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.chibatching.kotpref.blockingBulk
import com.kamran.weatherforcast.BuildConfig
import com.kamran.weatherforcast.R
import com.kamran.weatherforcast.data.api.ApiClientProvider
import com.kamran.weatherforcast.data.api.Resource
import com.kamran.weatherforcast.utils.ConfigPref
import com.kamran.weatherforcast.utils.UserInfo
import kotlinx.coroutines.Dispatchers
import java.util.*

/**
 * Created by Kamran Noorinejad on 5/13/2020 AD 12:55.
 * Edited by Kamran Noorinejad on 5/13/2020 AD 12:55.
 */
class SplashActivityViewModel(
    private val context: Context,
    apiProvider: ApiClientProvider
) : ViewModel() {

    private val api = apiProvider.createApiClient()

    var appVersionText: String =
        "v${BuildConfig.VERSION_NAME} Ⓒ ${Calendar.getInstance().get(Calendar.YEAR)}"
    var isUserLoggedIn: Boolean

    init {
        UserInfo.loggedIn.let {
            isUserLoggedIn = it
        }
    }

    fun checkIfNewVersionAvailable(): Boolean {
        if (ConfigPref.new_version_title != BuildConfig.VERSION_NAME) return true
        return false
    }

    // fun getInitialConfig() = liveData(Dispatchers.IO) {
    /* Resourceemit(Resource.loading())
     try {
         val response = api.getConfig()
         if (response.isSuccessful) {
             emit(
                 Resource.success(
                     response.body()?.status,
                     response.body()?.response?.message,
                     response.body()?.response?.data
                 )
             )
         } else {
             emit(
                 Resource.error(
                     response.body()?.status,
                     response.body()?.response?.message,
                     null
                 )
             )
         }
     } catch (e: Exception) {
         e.printStackTrace()
         emit(
             Resource.error(
                 false,
                 context.getString(R.string.network_error_text),
                 null
             )
         )
     }*/
    //}

    override fun onCleared() {
        super.onCleared()
    }
}