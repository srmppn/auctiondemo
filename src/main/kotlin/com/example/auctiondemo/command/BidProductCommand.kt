package com.example.auctiondemo.command

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.math.BigDecimal

data class BidProductCommand(
    @TargetAggregateIdentifier
    val productId: String,
    val currentBidOwner: String,
    val currentHighestBid: BigDecimal
)
