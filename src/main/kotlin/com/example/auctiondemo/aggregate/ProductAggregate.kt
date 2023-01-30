package com.example.auctiondemo.aggregate

import com.example.auctiondemo.command.CreateProductCommand
import com.example.auctiondemo.command.CreateProductEvent
import com.example.auctiondemo.domain.BidStatus
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import java.math.BigDecimal

@Aggregate
class ProductAggregate() {

    @AggregateIdentifier
    private lateinit var productId: String
    private lateinit var name: String
    private lateinit var description: String
    private lateinit var startPrice: BigDecimal

    @CommandHandler
    constructor(command: CreateProductCommand) : this(){
        AggregateLifecycle.apply(CreateProductEvent(
            command.productId,
            command.name,
            command.description,
            command.startPrice,
            null,
            null,
            null,
            BidStatus.NONE))
    }

    @EventSourcingHandler
    fun on (event: CreateProductEvent){
        productId = event.productId
        name = event.name
        description = event.description
        startPrice = event.startPrice
    }

}