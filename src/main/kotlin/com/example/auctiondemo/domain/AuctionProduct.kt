package com.example.auctiondemo.domain

import com.google.type.DateTime
import lombok.Getter
import lombok.Setter
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.Date

@Getter
@Setter
@Document
data class AuctionProduct (
    @Id
    val productId: String,
    val name: String,
    val description: String,
    val startPrice: BigDecimal,
    var currentBidOwner: String?,
    var currentHighestBid: BigDecimal?,
    var endedDateTime:Date?,
    val status: BidStatus
)