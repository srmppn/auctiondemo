package com.example.auctiondemo.command

import com.example.auctiondemo.domain.BidStatus
import java.util.*

data class StartAuctionEvent (
    val productId: String,
    val endedDateTime: Date,
    val status: BidStatus
)