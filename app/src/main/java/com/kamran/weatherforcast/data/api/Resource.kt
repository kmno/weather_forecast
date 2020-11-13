/*
 * Creator: Kamran Noorinejad on 5/19/20 12:23 PM
 * Last modified: 5/19/20 12:23 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */
package com.kamran.weatherforcast.data.api

/**
 * Created by Kamran Noorinejad on 5/19/2020 AD 12:23.
 * Edited by Kamran Noorinejad on 5/19/2020 AD 12:23.
 */
data class Resource<out T>(
    val state: State,
    val cod: Int?,
    val message: String?,
    val data: T?
) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(State.SUCCESS, 0, "", data)
        }

        fun <T> error(cod: Int?, message: String?, data: T? = null): Resource<T> {
            return Resource(State.ERROR, cod, message, data)
        }

        fun <T> loading(): Resource<T> {
            return Resource(State.LOADING, null, null, null)
        }
    }
}

enum class State {
    SUCCESS,
    ERROR,
    LOADING
}