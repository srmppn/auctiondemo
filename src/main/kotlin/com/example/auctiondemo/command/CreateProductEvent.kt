package com.example.auctiondemo.command

import com.example.auctiondemo.domain.BidStatus
import com.google.type.DateTime
import org.axonframework.serialization.Revision
import java.math.BigDecimal

@Revision("1.0")
data class CreateProductEvent(
    val productId: String,
    val name: String,
    val description: String,
    val startPrice: BigDecimal,
    val currentBidOwner: String?,
    val currentHighestBid: BigDecimal?,
    val endedDateTime: DateTime?,
    val status: BidStatus
)
