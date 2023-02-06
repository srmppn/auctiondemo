package com.example.auctiondemo.dto

import javax.validation.constraints.Min


data class StartAuctionRequest(
    @Min(value = 1, message = "Duration can't be negative.")
    val durationMin: Long
)
