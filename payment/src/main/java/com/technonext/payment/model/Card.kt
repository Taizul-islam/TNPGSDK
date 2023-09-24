package com.technonext.payment.model

data class Card(
    val cardTypeList: List<CardType>,
    val channelMap: ChannelMap
)