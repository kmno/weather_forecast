package com.kamran.weatherforcast.core

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.request.CachePolicy
import coil.util.CoilUtils
import com.kamran.weatherforcast.di.apiModule
import com.kamran.weatherforcast.di.utilsModule
import com.kamran.weatherforcast.di.viewModelModule
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Created by Kamran Noorinejad on 11/7/2020 AD 13:40.
 * Edited by Kamran Noorinejad on 11/7/2020 AD 13:40.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@App)
            modules(
                viewModelModule,
                utilsModule,
                apiModule
            )
        }
    }
}