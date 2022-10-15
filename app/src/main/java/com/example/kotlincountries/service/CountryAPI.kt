package com.example.kotlincountries.service

import com.example.kotlincountries.model.Country
import io.reactivex.Single
import retrofit2.http.GET

interface CountryAPI  {

    //GET , POST
    //Base_url -> https://raw.githubusercontent.com/
    //EXT -> atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json

    @GET("atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json")
    fun getCountries() : Single<List<Country>>
}