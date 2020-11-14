/*
 * Creator: Kamran Noorinejad on 9/23/20 4:01 PM
 * Last modified: 9/23/20 4:01 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kamran.weatherforcast.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Kamran Noorinejad on 9/23/2020 AD 16:01.
 * Edited by Kamran Noorinejad on 9/23/2020 AD 16:01.
 */
object DateHelper {

    @SuppressLint("SimpleDateFormat")
    fun convertLongToTime(time: Long): String {
        val date = Date(time * 1000)
        val format = SimpleDateFormat("ha")
        return format.format(date)
    }

}