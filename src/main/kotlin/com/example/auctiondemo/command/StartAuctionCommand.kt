package com.example.auctiondemo.command

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class StartAuctionCommand(
    @TargetAggregateIdentifier
    val productId: String,
    val durationMin: Long
)
