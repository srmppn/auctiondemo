package com.example.auctiondemo.dto

import java.math.BigDecimal

data class StartAuctionRequest(
    val durationMin: BigDecimal?
)
