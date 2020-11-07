package com.kamran.weatherforcast.core

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.request.CachePolicy
import coil.util.CoilUtils
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Created by Kamran Noorinejad on 11/7/2020 AD 13:40.
 * Edited by Kamran Noorinejad on 11/7/2020 AD 13:40.
 */
class App : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@App)
            modules(
                //viewModelModule,
               // utilsModule,
               // apiModule
            )
        }

        Coil.setImageLoader(newImageLoader())

    }

    //create default image loader
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this@App)
            .availableMemoryPercentage(0.25)
            .crossfade(true)
            .allowHardware(false)
            .diskCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(CachePolicy.ENABLED)
            //.placeholder(R.drawable.placeholder_trans)
            .okHttpClient {
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(this@App))
                    .build()
            }
            .build()
    }
}