package com.example.auctiondemo.command

import com.example.auctiondemo.domain.BidStatus
import org.axonframework.serialization.Revision
import java.sql.Timestamp

@Revision("1.0")
data class StartAuctionEvent(
    val productId: String,
    val endedDateTime: Timestamp,
    val status: BidStatus
)
