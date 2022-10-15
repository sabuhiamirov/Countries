package com.example.kotlincountries.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlincountries.R
import com.example.kotlincountries.databinding.ItemCountryBinding
import com.example.kotlincountries.model.Country
import com.example.kotlincountries.util.downloadFromUrl
import com.example.kotlincountries.util.placeholderProgressBar


import com.example.kotlincountries.view.FeedFragmentDirections
import kotlinx.android.synthetic.main.country_info_fragment.view.*
import kotlinx.android.synthetic.main.item_country.view.*


class CountryAdapter(val counrtyList : ArrayList<Country>) : RecyclerView.Adapter<CountryAdapter.CountryAdapterHolder>(),CountryClickListener {

    class CountryAdapterHolder(val view : ItemCountryBinding) : RecyclerView.ViewHolder(view.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryAdapterHolder {
        val inflater = LayoutInflater.from(parent.context)
     //   val view = inflater.inflate(R.layout.item_country,parent,false)
        val view=DataBindingUtil.inflate<ItemCountryBinding>(inflater,R.layout.item_country,parent,false)
        return CountryAdapterHolder(view)
    }

    override fun onBindViewHolder(holder: CountryAdapterHolder, position: Int) {
        holder.view.country=counrtyList[position]
        holder.view.listener=this

   /*     holder.view.countryNameTxt.text=counrtyList.get(position).countryName
        holder.view.countryRegionTxt.text=counrtyList.get(position).countryRegion

        holder.view.countryFlagImage.downloadFromUrl(counrtyList[position].countryFlagImg,
            placeholderProgressBar(holder.view.context)
        )

        holder.view.setOnClickListener{
            val action=FeedFragmentDirections.actionFeedFragmentToCountryInfoFragment(counrtyList[position].uuid)
            Navigation.findNavController(it).navigate(action)
        }*/

    }

    override fun getItemCount(): Int {
       return counrtyList.size
    }


    fun updateCountryList(newCountryList : List<Country>){
        counrtyList.clear()
        counrtyList.addAll(newCountryList)
        notifyDataSetChanged()
    }

    override fun onCounrtyClicked(v: View) {
        val uuid=v.countryUuidTXT.text.toString().toLong()
        val action=FeedFragmentDirections.actionFeedFragmentToCountryInfoFragment(uuid)
        Navigation.findNavController(v).navigate(action)
    }
}