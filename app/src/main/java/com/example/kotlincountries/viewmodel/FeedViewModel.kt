package com.example.kotlincountries.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlincountries.model.Country
import com.example.kotlincountries.service.CountryAPIService
import com.example.kotlincountries.service.CountryDatabase
import com.example.kotlincountries.util.CustomSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.operators.single.SingleObserveOn
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : BaseViewModel(application) {

    private val countryApiService = CountryAPIService()
    private val disposable = CompositeDisposable()
    private var customPreferences = CustomSharedPreferences(getApplication())
    private var refreshTime = 10 * 60 * 1000 * 1000 * 1000L

    val countries=MutableLiveData<List<Country>>()
    val countryError=MutableLiveData<Boolean>()
    val countryLoading=MutableLiveData<Boolean>()

    fun refreshData(){
        val updateTime=customPreferences.getTime()
        if (updateTime!=null && updateTime !=0L &&System.nanoTime()-updateTime<refreshTime){
           getDataFromSQLite()
        }else{
            getDataFromAPI()
        }


    }

    fun getDataFromSQLite(){
        countryLoading.value=true

        launch {
            val countries=CountryDatabase(getApplication()).countryDao().getAllCountries()
            showCoroutines(countries)
            Toast.makeText(getApplication(),"Countries from SQLite",Toast.LENGTH_LONG).show()
        }
    }

    fun refreshFromAPI(){
        getDataFromAPI()
    }

    private fun getDataFromAPI(){
        countryLoading.value=true

        disposable.add(
            countryApiService.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Country>>(){
                    override fun onSuccess(t: List<Country>) {
                        storeInSQLite(t)
                        Toast.makeText(getApplication(),"Countries from API",Toast.LENGTH_LONG).show()

                    }

                    override fun onError(e: Throwable) {
                        countryLoading.value=false
                        countryError.value=true
                    }

                })

        )
    }

    private fun showCoroutines(countryList : List<Country>){
        countries.value=countryList
        countryLoading.value=false
        countryError.value=false
    }

    private fun storeInSQLite(list : List<Country>){
        launch {
            val dao=CountryDatabase(getApplication()).countryDao()
            dao.DeleteAllCountries()
            val listLong=dao.insertAll(*list.toTypedArray()) // list -> individual

            var i=0
            while (i<list.size){
                list[i].uuid=listLong[i].toLong()
                i++
            }

            showCoroutines(list)
        }

        customPreferences.saveTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()

        disposable.clear()
    }
}
