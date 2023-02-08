package com.example.auctiondemo.api.command

import com.example.auctiondemo.domain.BidStatus

data class AuctionEndedEvent(
    val productId: String,
    val status: BidStatus
)