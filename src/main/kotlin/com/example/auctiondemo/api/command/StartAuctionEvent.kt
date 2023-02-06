package com.example.auctiondemo.api.command

import com.example.auctiondemo.domain.BidStatus
import java.util.*

data class StartAuctionEvent (
    val productId: String,
    val endedDateTime: Date,
    val status: BidStatus
)