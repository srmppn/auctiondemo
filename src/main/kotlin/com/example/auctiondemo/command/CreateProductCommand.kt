package com.example.auctiondemo.command

import com.example.auctiondemo.domain.BidStatus
import com.google.type.DateTime
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.math.BigDecimal

data class CreateProductCommand(
    @TargetAggregateIdentifier
    val productId: String,
    val name: String,
    val description: String,
    val startPrice: BigDecimal,
//    val currentBidOwner: String?,
//    val currentHighestBid: BigDecimal?,
//    val endedDateTime: DateTime?,
//    val status: BidStatus
)
