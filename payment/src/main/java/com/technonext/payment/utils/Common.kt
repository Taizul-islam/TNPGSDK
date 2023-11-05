package com.technonext.payment.utils

import android.content.Context
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.technonext.payment.adapter.CardTypeAdapter
import com.technonext.payment.model.CardType


object Common {
    var LIVE_URL=""
    var TEST_URL=""
    var LOGIN_URL=""
    var SAVE_URL=""
    var VERIFY_BASE_URL=""
    var ALL_URL=""
    var  SELECTED_ID = 0
    var SELECTED_TITLE=""
    var sdk=SDKType.TEST
    fun setData(context: Context, recyclerView: RecyclerView,adapter: CardTypeAdapter) {
        val lManager = GridLayoutManager(context,3,LinearLayoutManager.VERTICAL,false)
        recyclerView.layoutManager = lManager
        val dividerItemDecoration =
            DividerItemDecoration(recyclerView.context, lManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.adapter = adapter
        adapter.setOnClickListener(object :
            CardTypeAdapter.OnClickListener {
            override fun onClick(position: Int, model: CardType) {
                SELECTED_ID = model.id
                SELECTED_TITLE=model.name
            }
        })




    }
}