package com.example.auctiondemo.api.command

import com.example.auctiondemo.domain.BidStatus
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.math.BigDecimal

data class StartAuctionCommand(
    @TargetAggregateIdentifier
    val productId: String,
    val durationMin: Long
)
