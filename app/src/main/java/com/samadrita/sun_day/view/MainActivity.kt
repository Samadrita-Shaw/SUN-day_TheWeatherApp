package com.samadrita.sun_day.view

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.samadrita.sun_day.R
import com.samadrita.sun_day.databinding.ActivityMainBinding
import com.samadrita.sun_day.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewmodel: MainViewModel

    private lateinit var GET:SharedPreferences
    private lateinit var SET:SharedPreferences.Editor

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GET = getSharedPreferences(packageName, MODE_PRIVATE)
        SET = GET.edit()

        viewmodel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        var cName = GET.getString("cityName","jamshedpur")
        binding.edtCityName.setText(cName)

        viewmodel.refreshData(cName!!)

        getLiveData()

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.llDataView.visibility = View.GONE
            binding.tvError.visibility = View.GONE
            binding.pbLoading.visibility = View.GONE

            var cityName = GET.getString("cityName",cName)
            binding.edtCityName.setText(cityName)
            viewmodel.refreshData(cityName!!)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.imgSearchCityName.setOnClickListener{
            val cityName = binding.edtCityName.text.toString()
            SET.putString("cityName", cityName)
            SET.apply()
            viewmodel.refreshData(cityName)
            getLiveData()

        }


    }

    private fun getLiveData() {
        viewmodel.weather_data.observe(this,{data ->
            data?.let {
                binding.llDataView.visibility = View.VISIBLE
                binding.pbLoading.visibility = View.GONE

                binding.tvDegree.text = data.main.temp.toString() + "°C"
                binding.tvCountryCode.text = data.sys.country.toString()
                binding.tvCityName.text = data.name.toString()
                binding.tvHumidity.text = " : "+data.main.humidity.toString() + "%"
                binding.tvSpeed.text = " : "+data.wind.speed.toString() + "m/s"
                binding.tvLat.text = " : "+data.coord.lat.toString()
                binding.tvLon.text = " : "+data.coord.lon.toString()

                Glide.with(this).load("http://openweathermap.org/img/wn/" + data.weather.get(0).icon + "@2x.png")
                    .into(binding.imgWeatherIcon)
            }
        })

        viewmodel.weather_load.observe(this, { load ->
            load?.let {
                if(it){
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                    binding.llDataView.visibility = View.GONE
                }else{
                    binding.pbLoading.visibility = View.GONE
                }
            }
        })

        viewmodel.weather_error.observe(this, { error ->
            error?.let {
                if (it){
                    binding.tvError.visibility = View.VISIBLE
                    binding.llDataView.visibility = View.GONE
                    binding.pbLoading.visibility = View.GONE
                }
                else{
                    binding.tvError.visibility = View.GONE
                }
            }

        })
    }
}