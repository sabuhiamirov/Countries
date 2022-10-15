package com.example.kotlincountries.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kotlincountries.R
import com.example.kotlincountries.databinding.CountryInfoFragmentBinding
import com.example.kotlincountries.util.downloadFromUrl
import com.example.kotlincountries.util.placeholderProgressBar
import com.example.kotlincountries.viewmodel.CountryViewModel
import kotlinx.android.synthetic.main.country_info_fragment.*
import kotlinx.android.synthetic.main.item_country.*

class CountryInfoFragment : Fragment() {

    private lateinit var dataBinding : CountryInfoFragmentBinding
    private lateinit var viewModel : CountryViewModel
    private var countryUuid =0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding= DataBindingUtil.inflate(inflater,R.layout.country_info_fragment,container,false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            countryUuid=CountryInfoFragmentArgs.fromBundle(it).countryUuid
        }

        viewModel=ViewModelProvider(this)[CountryViewModel::class.java]
        viewModel.getDataRoom(countryUuid)

        observeLiveData()
    }

    fun observeLiveData(){
        viewModel.countryLiveData.observe(viewLifecycleOwner, Observer { country ->
            country?.let {
                dataBinding.selectedCountry=country
   /*             infoCountryNameTxt.text=country.countryName
                infoCountryRegionTxt.text=country.countryRegion
                infoCountryCapitalTxt.text=country.countryCapital
                infoCountryLanguageTxt.text=country.countryLanguage
                infoCountryCurremcyTxt.text=country.countryCurrency
                context?.let {
                    infoCountryFlagImg.downloadFromUrl(country.countryFlagImg, placeholderProgressBar(it))
                }*/
            }

        })
    }
}