package com.example.auctiondemo.api.command

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
    val status: BidStatus
)
