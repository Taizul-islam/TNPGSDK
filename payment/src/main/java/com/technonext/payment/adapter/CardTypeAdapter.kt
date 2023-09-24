package com.technonext.payment.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.technonext.payment.R
import com.technonext.payment.model.Card
import com.technonext.payment.model.CardType
import com.technonext.payment.utils.Common


public class CardTypeAdapter(context: Context, data: List<CardType>, from: String) :
    RecyclerView.Adapter<CardTypeAdapter.MyViewHolder>() {
    private var onClickListener: OnClickListener? = null
    var inflater: LayoutInflater
    var list = emptyList<CardType>()
    var cardList= emptyList<CardType>()
    var mobileList= emptyList<CardType>()
    var netList= emptyList<CardType>()
    var mContext: Context
    var from: String = ""


    init {
        inflater = LayoutInflater.from(context)
        mContext = context
        this.list = data
        this.from = from
        if(from=="card"){
            cardList=list.filter { s->s.category==1 }
        }
        if(from=="mobile"){
            mobileList=list.filter { s->s.category==3 }
        }
        if(from=="net"){
            netList=list.filter { s->s.category==2 }
        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            inflater.inflate(R.layout.bank_list_recycler, parent, false), viewType
        )
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {

        val item = if(from=="mobile") mobileList[position] else if(from=="card") cardList[position] else netList[position]
        Log.d("ADAPTER", "onBindViewHolder: ${item.id}")
        Glide
            .with(mContext)
            .load(item.logoUrl)
            .centerCrop()
            .into(holder.merchantImage)

        holder.tvName.text = item.name
        holder.tvStatus.text = item.isActive.toString()
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                for (i in mobileList){
                    i.isSelected=false
                    Log.d("SELECTEDMOBILE", "onBindViewHolder: ${i.isSelected}")
                }
                for (i in cardList){
                    i.isSelected=false
                    Log.d("SELECTEDLOG", "onBindViewHolder: ${i.isSelected}")
                }
                for (i in netList){
                    i.isSelected=false
                    Log.d("SELECTEDLOG", "onBindViewHolder: ${i.isSelected}")
                }


                for (i in list){
                    Log.d("SELECTEDLOG", "onBindViewHolder: ${i.isSelected}")
                }


                item.isSelected=true
                notifyDataSetChanged()

                Log.d("FROMAAAAAA", "onBindViewHolder: ${item.isSelected}")
//                if (from == "card") {
//
//
//
//
//                    val previousSelectedPosition = Common.SELECTED_POSITION_CARD
//                    Common.SELECTED_POSITION_CARD = holder.adapterPosition
//                    notifyItemChanged(previousSelectedPosition)
//                    notifyItemChanged(Common.SELECTED_POSITION_CARD)
//
//                }
//                if (from == "mobile") {
//
//
//
//                    val previousSelectedPosition = Common.SELECTED_POSITION_MOBILE
//                    Common.SELECTED_POSITION_MOBILE = holder.adapterPosition
//                    notifyItemChanged(previousSelectedPosition)
//                    notifyItemChanged(Common.SELECTED_POSITION_MOBILE)
//                }
//                if (from == "net") {
//
//
//
//                    val previousSelectedPosition = Common.SELECTED_POSITION_NET
//                    Common.SELECTED_POSITION_NET = holder.adapterPosition
//                    notifyItemChanged(previousSelectedPosition)
//                    notifyItemChanged(Common.SELECTED_POSITION_NET)
//                }
                onClickListener!!.onClick(position, item)
            }
        }

        if(item.isSelected){
            holder.iv_status.setColorFilter(Color.GREEN)
        }else{
            holder.iv_status.setColorFilter(Color.GRAY)
        }





//        if (position == Common.SELECTED_POSITION_CARD&&from=="card") {
//            Common.SELECTED_POSITION_MOBILE=RecyclerView.NO_POSITION
//            Common.SELECTED_POSITION_NET=RecyclerView.NO_POSITION
//            notifyItemChanged(RecyclerView.NO_POSITION)
//            holder.iv_status.setColorFilter(Color.GREEN)
//        }
//        else if (position == Common.SELECTED_POSITION_MOBILE&&from=="mobile") {
//            Common.SELECTED_POSITION_CARD=RecyclerView.NO_POSITION
//            Common.SELECTED_POSITION_NET=RecyclerView.NO_POSITION
//            notifyItemChanged(RecyclerView.NO_POSITION)
//            holder.iv_status.setColorFilter(Color.GREEN)
//        }
//        else if (position == Common.SELECTED_POSITION_NET&&from=="net") {
//            Common.SELECTED_POSITION_MOBILE=RecyclerView.NO_POSITION
//            Common.SELECTED_POSITION_CARD=RecyclerView.NO_POSITION
//            holder.iv_status.setColorFilter(Color.GREEN)
//        } else {
//            holder.iv_status.setColorFilter(Color.GRAY)
//        }


    }


    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, model: CardType)
    }

    override fun getItemCount(): Int {
        if(from=="mobile"){
            return mobileList.size
        }
        if(from=="card"){
            return cardList.size
        }else{
            return netList.size
        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class MyViewHolder(itemView: View, viewType: Int) :
        RecyclerView.ViewHolder(itemView) {
        var merchantImage: ImageView
        var iv_status: ImageView
        var tvName: TextView
        var tvStatus: TextView

        init {
            merchantImage = itemView.findViewById<View>(R.id.imgLogo) as ImageView
            iv_status = itemView.findViewById<View>(R.id.iv_status) as ImageView
            tvName = itemView.findViewById<View>(R.id.tv_name) as TextView
            tvStatus = itemView.findViewById<View>(R.id.tv_status) as TextView
            //selectedItemPosition=adapterPosition

        }
    }
}