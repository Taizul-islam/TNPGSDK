package com.technonext.payment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.technonext.payment.R
import com.technonext.payment.model.CardType


class CardTypeAdapter(context: Context, data: List<CardType>, from: String) :
    RecyclerView.Adapter<CardTypeAdapter.MyViewHolder>() {
    private var onClickListener: OnClickListener? = null
    private var inflater: LayoutInflater
    private var list = emptyList<CardType>()
    private var cardList = emptyList<CardType>()
    private var mobileList = emptyList<CardType>()
    private var netList = emptyList<CardType>()
    private var mContext: Context
    private var from: String = ""


    init {
        inflater = LayoutInflater.from(context)
        mContext = context
        this.list = data
        this.from = from
        cardList = list.filter { s -> s.category == 1 }
        mobileList = list.filter { s -> s.category == 3 }
        netList = list.filter { s -> s.category == 2 }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            inflater.inflate(R.layout.bank_list_recycler, parent, false)
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {

        val item =
            if (from == "mobile") mobileList[position] else if (from == "card") cardList[position] else netList[position]

        Glide
            .with(mContext)
            .load(item.logoUrl)
            .centerCrop()
            .into(holder.merchantImage)

        holder.tvName.text = item.name
        holder.tvStatus.text = item.isActive.toString()
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                for (i in mobileList) {
                    i.isSelected = false
                }
                for (i in cardList) {
                    i.isSelected = false
                }
                for (i in netList) {
                    i.isSelected = false
                }
                item.isSelected = true
                notifyDataSetChanged()
                onClickListener!!.onClick(position, item)
            }
        }

        if (item.isSelected) {
            holder.ivStatus.setColorFilter(Color.GREEN)
        } else {
            holder.ivStatus.setColorFilter(Color.GRAY)
        }


    }


    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: CardType)
    }

    override fun getItemCount(): Int {
        if (from == "mobile") {
            return mobileList.size
        }
        return if (from == "card") {
            cardList.size
        } else {
            netList.size
        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var merchantImage: ImageView
        var ivStatus: ImageView
        var tvName: TextView
        var tvStatus: TextView

        init {
            merchantImage = itemView.findViewById<View>(R.id.imgLogo) as ImageView
            ivStatus = itemView.findViewById<View>(R.id.iv_status) as ImageView
            tvName = itemView.findViewById<View>(R.id.tv_name) as TextView
            tvStatus = itemView.findViewById<View>(R.id.tv_status) as TextView

        }
    }
}