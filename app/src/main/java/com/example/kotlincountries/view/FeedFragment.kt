package com.example.kotlincountries.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlincountries.R
import com.example.kotlincountries.adapter.CountryAdapter
import com.example.kotlincountries.viewmodel.FeedViewModel
import kotlinx.android.synthetic.main.feed_fragment.*

class FeedFragment : Fragment() {

    private lateinit var viewModel : FeedViewModel
    private var countryAdapter = CountryAdapter(arrayListOf())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.feed_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[FeedViewModel::class.java]
        viewModel.refreshData()

        countriesFeedRecycle.layoutManager=LinearLayoutManager(context)
        countriesFeedRecycle.adapter=countryAdapter

        SwipeRefreshLayoutFeedCountry.setOnRefreshListener {
            countriesFeedRecycle.visibility=View.GONE
            errDonwloadText.visibility=View.GONE
            loadingCountryRecycle.visibility=View.VISIBLE

            viewModel.refreshFromAPI()

            SwipeRefreshLayoutFeedCountry.isRefreshing=false
        }
        observeLiveData()
    }

    fun observeLiveData(){
        viewModel.countries.observe(viewLifecycleOwner,Observer{ countries->
            countries?.let{
                countriesFeedRecycle.visibility=View.VISIBLE
                countryAdapter.updateCountryList(countries)
            }
        })

        viewModel.countryError.observe(viewLifecycleOwner, Observer {error->
            error?.let {
                if (it){
                    errDonwloadText.visibility=View.VISIBLE
                    countriesFeedRecycle.visibility=View.GONE
                }else{
                    errDonwloadText.visibility=View.GONE
                }
            }

        })

        viewModel.countryLoading.observe(viewLifecycleOwner, Observer {  loading->
            loading?.let {
                if (it){
                    loadingCountryRecycle.visibility=View.VISIBLE
                    errDonwloadText.visibility=View.GONE
                    countriesFeedRecycle.visibility=View.GONE
                }else{loadingCountryRecycle.visibility=View.GONE}
            }

        })
    }
}