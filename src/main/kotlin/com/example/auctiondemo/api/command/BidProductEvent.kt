package com.example.auctiondemo.api.command

import org.axonframework.serialization.Revision
import java.math.BigDecimal

@Revision("1.0")
data class BidProductEvent(
    val productId: String,
    val currentBidOwner: String,
    val currentHighestBid: BigDecimal
)
