package com.technonext.payment.fragment

import HomeViewModel
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.cheezycode.randomquote.viewmodels.HomeViewModelFactory
import com.technonext.payment.R
import com.technonext.payment.adapter.CardTypeAdapter
import com.technonext.payment.model.CardType
import com.technonext.payment.utils.App
import com.technonext.payment.utils.Common


class MobileFragment : Fragment() {
    lateinit var mainViewModel: HomeViewModel
    private var adapter: CardTypeAdapter? = null
    var list= emptyList<CardType>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_card, container, false)
        val repository = (requireActivity().application as App).quoteRepository
        val mobileBankingRecycler = view.findViewById<RecyclerView>(R.id.recyclerView)
        mainViewModel = ViewModelProvider(requireActivity(), HomeViewModelFactory(repository)).get(HomeViewModel::class.java)
        mainViewModel.cardListResponse?.observe(requireActivity(), Observer{
            if (it != null) {
                if(it.cardTypeList.isNotEmpty()){
                    list=it.cardTypeList
                    adapter= CardTypeAdapter(requireActivity(),list,"mobile")
                    Common.setData(requireActivity(),mobileBankingRecycler,adapter!!)
                }
            }
        })

        return view
    }

    override fun onResume() {
        super.onResume()

        if (list.isNotEmpty()){
            Log.d("AAAAAAAAA", "onResume: Exxx")
            adapter!!.notifyDataSetChanged()
        }
    }



}