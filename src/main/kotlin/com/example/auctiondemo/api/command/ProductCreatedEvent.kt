package com.example.auctiondemo.api.command

import com.example.auctiondemo.domain.BidStatus
import org.axonframework.serialization.Revision
import java.math.BigDecimal

@Revision("1.0")
data class ProductCreatedEvent(
    val productId: String,
    val name: String,
    val description: String,
    val startPrice: BigDecimal,
    val status: BidStatus
)
