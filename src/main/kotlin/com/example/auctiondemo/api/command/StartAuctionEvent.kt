package com.example.auctiondemo.api.command

import com.example.auctiondemo.domain.BidStatus
import java.time.Instant

data class StartAuctionEvent (
    val productId: String,
    val endedDateTime: Instant,
    val status: BidStatus
)