package com.example.auctiondemo.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.Instant
import java.util.Date

@Document
data class AuctionProduct (
    @Id
    val productId: String,
    val name: String,
    val description: String,
    val startPrice: BigDecimal,
    var currentBidOwner: String?,
    var currentHighestBid: BigDecimal?,
    var endedDateTime:Instant?,
    val status: BidStatus
)