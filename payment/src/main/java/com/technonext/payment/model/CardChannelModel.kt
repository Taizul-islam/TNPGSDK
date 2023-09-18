package com.technonext.payment.model

data class CardChannelModel(
    val cardTypeSet: List<CardTypeSet>,
    val id: Int,
    val isActive: Boolean,
    val name: String,
    val title: String
)