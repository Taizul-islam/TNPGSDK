package com.technonext.payment.model

data class CardType(
    val category: Int,
    val channelIds: List<Int>,
    val id: Int,
    val isActive: Boolean,
    var isSelected: Boolean=false,
    val logoUrl: String,
    val name: String
)