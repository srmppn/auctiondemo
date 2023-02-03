package com.example.auctiondemo.aggregate

import com.example.auctiondemo.command.CreateProductCommand
import com.example.auctiondemo.command.CreateProductEvent
import com.example.auctiondemo.command.StartAuctionCommand
import com.example.auctiondemo.command.StartAuctionEvent
import com.example.auctiondemo.domain.BidStatus
import com.google.type.DateTime
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

@Aggregate
class ProductAggregate() {

    @AggregateIdentifier
    private lateinit var productId: String
    private lateinit var name: String
    private lateinit var description: String
    private lateinit var startPrice: BigDecimal
    private lateinit var endedDateTime: Timestamp
    private lateinit var status: BidStatus

    @CommandHandler
    constructor(command: CreateProductCommand) : this(){
        AggregateLifecycle.apply(CreateProductEvent(
            command.productId,
            command.name,
            command.description,
            command.startPrice,
//            null,
//            null,
//            null,
            BidStatus.NONE))
    }

    @EventSourcingHandler
    fun on (event: CreateProductEvent){
        productId = event.productId
        name = event.name
        description = event.description
        startPrice = event.startPrice
    }

    @CommandHandler
    fun handle(command : StartAuctionCommand) : String{
        val endedDate = Timestamp (System.currentTimeMillis() + (command.durationMin*60*1000))
        AggregateLifecycle.apply(StartAuctionEvent(command.productId, endedDate , BidStatus.STARTED))
        return "Auction start!!"
    }

    @EventSourcingHandler
    fun oc(event: StartAuctionEvent){
        productId = event.productId
        endedDateTime = event.endedDateTime
        status = event.status
    }

}