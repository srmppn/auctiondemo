package com.example.auctiondemo.dto

import java.math.BigDecimal

data class BidProductRequest(
    val name: String,
    val bidAmount: BigDecimal
)
