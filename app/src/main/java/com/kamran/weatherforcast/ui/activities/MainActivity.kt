package com.kamran.weatherforcast.ui.activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.kamran.weatherforcast.R
import com.kamran.weatherforcast.data.api.State
import com.kamran.weatherforcast.data.model.Current
import com.kamran.weatherforcast.data.model.Daily
import com.kamran.weatherforcast.ui.viewmodels.HomeActivityViewModel
import com.kamran.weatherforcast.utils.DateHelper
import com.link184.kidadapter.setUp
import com.link184.kidadapter.simple.SingleKidAdapter
import com.soywiz.klock.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.daily_recyclerview_list_item.view.*
import kotlinx.android.synthetic.main.hourly_recyclerview_list_item.view.*
import kotlinx.android.synthetic.main.main_content.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.Date
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private val homeActivityViewModel: HomeActivityViewModel by viewModel()

    private lateinit var hourlyAdapter: SingleKidAdapter<Current>
    private lateinit var dailyAdapter: SingleKidAdapter<Daily>

    private var hours = mutableListOf<Current>()
    private var days = mutableListOf<Daily>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_WeatherForcast)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpScreen()

        setBackgroundColor()

        getCurrentWeatherForecast()


        //   val yourmilliseconds = System.currentTimeMillis()
        val yourmilliseconds = 1605250800L
        val sdf = SimpleDateFormat("MMM dd,yyyy HH:mm")
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val resultdate = Date(yourmilliseconds)

    }


    @SuppressLint("SimpleDateFormat")
    private fun setBackgroundColor() {
        var drw = R.drawable.morning_bg
        val local = DateTimeTz.nowLocal().hours
        Log.e(
            "NOW",
            local.toString()
        )

        when (local) {
            in 0..6 -> {
                drw = R.drawable.dawn_bg
                Log.e("NOW is", "dawn")
            }//min
            in 7..11 -> {
                drw = R.drawable.morning_bg
                Log.e("NOW is", "morning")
            }//morn
            in 12..16 -> {
                drw = R.drawable.noon_bg
                Log.e("NOW is", "noon")
            }//max
            in 17..19 -> {
                drw = R.drawable.evening_bg
                Log.e("NOW is", "evening")
            }//eve
            in 20..23 -> {
                drw = R.drawable.night_bg
                Log.e("NOW is", "night")
            }//night
        }
        window.decorView.background = ContextCompat.getDrawable(this, drw)
    }

    @SuppressLint("SetTextI18n")
    private fun getCurrentWeatherForecast() {
        homeActivityViewModel.getWeatherForecast("51.421509", "35.694389")
            .observe(this, { networkResource ->
                when (networkResource.state) {
                    State.LOADING -> {
                        Log.e("NET", "loading")
                    }
                    State.SUCCESS -> {
                        Log.e("NET", networkResource.toString())
                        loading_bar.visibility = View.GONE
                        main_content.visibility = View.VISIBLE
                        city.text = networkResource.data?.timezone
                        current_temp.text = "${
                            networkResource.data?.current?.temp!!.toFloat().roundToInt()
                        }°"

                        weather_icon.setImageDrawable(
                            ContextCompat.getDrawable(
                                applicationContext,
                                resources.getIdentifier(
                                    networkResource.data.current.weather.first().description.replace(
                                        " ",
                                        "_"
                                    ),
                                    "drawable",
                                    applicationContext?.packageName
                                )
                            )
                        )

                        hours = networkResource.data.hourly.take(6).toMutableList()
                        feedHours()

                        days = networkResource.data.daily.take(7).toMutableList()
                        feedDays()
                    }
                    State.ERROR -> {
                        Log.e("NET", networkResource.message.toString())
                    }
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun feedHours() {
        val dateFormat: DateFormat = DateFormat("HH:mm:ss")
        hourlyAdapter = hourly_recyclerview.setUp<Current> {
            withLayoutManager(
                LinearLayoutManager(
                    applicationContext,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            )
            withLayoutResId(R.layout.hourly_recyclerview_list_item)
            withItems(hours)
            bindIndexed { hour, position ->
                //Log.e("DateTimeTz.now unix", DateTime.nowUnix().toLong().toString())
                Log.e("now unix", System.currentTimeMillis().toString())
                Log.e("unix", hour.dt.toString())

                Log.e("desc", hour.weather.first().description.replace(" ", "_"))
                hourly_temp.text = "${
                    hour.temp.toFloat().roundToInt()
                }°"
                hourly_time.text = DateHelper.convertLongToTime(hour.dt.toLong())

                try {
                    hourly_icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            applicationContext,
                            resources.getIdentifier(
                                hour.weather.first().description.replace(" ", "_"),
                                "drawable",
                                context?.packageName
                            )
                        )
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun feedDays() {
        val today = DateTimeTz.nowLocal()
        dailyAdapter = daily_recyclerview.setUp<Daily> {
            withLayoutResId(R.layout.daily_recyclerview_list_item)
            withItems(days)
            bindIndexed { day, position ->
                if (position == 0) day_title.text = "Tomorrow"
                else day_title.text =
                    (today + (position + 1).days).dayOfWeek.toString()
                day_temp.text = "${
                    day.temp.max.toFloat().roundToInt()
                }° / ${
                    day.temp.min.toFloat().roundToInt()
                }°"

                try {
                    day_weather_icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            applicationContext,
                            resources.getIdentifier(
                                day.weather.first().description.replace(" ", "_"),
                                "drawable",
                                context?.packageName
                            )
                        )
                    )
                } catch (e: Exception) {
                    //e.printStackTrace()
                }
            }
        }
    }

    private fun setUpScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }
}