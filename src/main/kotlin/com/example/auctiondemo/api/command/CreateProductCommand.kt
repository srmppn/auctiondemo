package com.example.auctiondemo.api.command

import com.example.auctiondemo.domain.BidStatus
import com.google.type.DateTime
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.math.BigDecimal

data class CreateProductCommand(
    @TargetAggregateIdentifier
    val productId: String,
    val name: String,
    val description: String,
    val startPrice: BigDecimal
)
