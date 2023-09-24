package com.technonext.payment.utils

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.technonext.payment.adapter.CardTypeAdapter
import com.technonext.payment.model.CardType

object Common {
    var  SELECTED_ID = 0
    var  SELECTED_POSITION_CARD = RecyclerView.NO_POSITION
    var  SELECTED_POSITION_MOBILE = RecyclerView.NO_POSITION
    var  SELECTED_POSITION_NET = RecyclerView.NO_POSITION
    fun setData(context: Context, recyclerView: RecyclerView,adapter: CardTypeAdapter) {
        val lManager = LinearLayoutManager(context)
        recyclerView.layoutManager = lManager
        recyclerView.adapter = adapter
        adapter.setOnClickListener(object :
            CardTypeAdapter.OnClickListener {
            override fun onClick(position: Int, model: CardType) {
                SELECTED_ID = model.id
            }
        })



    }
}