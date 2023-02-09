package com.example.auctiondemo.dto

import java.math.BigDecimal

data class CreateProductRequest (
    val name: String,
    val description: String,
    val startPrice: BigDecimal
)