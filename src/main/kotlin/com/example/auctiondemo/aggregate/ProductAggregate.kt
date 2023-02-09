package com.example.auctiondemo.aggregate

import com.example.auctiondemo.api.command.*
import com.example.auctiondemo.domain.BidStatus
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.deadline.DeadlineManager
import org.axonframework.deadline.annotation.DeadlineHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import java.lang.IllegalStateException
import java.math.BigDecimal
import java.time.Instant

@Aggregate
class ProductAggregate() {

    companion object {
        const val AUCTION_DEADLINE = "auction_deadline"
        private const val SECOND_IN_MINUTE = 60L
    }

    @AggregateIdentifier
    private lateinit var productId: String
    private lateinit var name: String
    private lateinit var description: String
    private lateinit var startPrice: BigDecimal
    private lateinit var endedDateTime: Instant
    private lateinit var status: BidStatus
    private lateinit var currentBidOwner: String
    private var currentHighestBid: BigDecimal = BigDecimal.ZERO


    @CommandHandler
    constructor(command: CreateProductCommand) : this(){
        AggregateLifecycle.apply(
            ProductCreatedEvent(
            command.productId,
            command.name,
            command.description,
            command.startPrice,
            BidStatus.NONE)
        )
    }

    @EventSourcingHandler
    fun on (event: ProductCreatedEvent){
        productId = event.productId
        name = event.name
        description = event.description
        startPrice = event.startPrice
        status = BidStatus.NONE
    }

    @CommandHandler
    fun handle(command : StartAuctionCommand, deadlineManager: DeadlineManager) : String{
        if(status == BidStatus.NONE){
            val endedDate = Instant.now().plusSeconds(command.durationMin*SECOND_IN_MINUTE)
            deadlineManager.schedule(endedDate, AUCTION_DEADLINE)
            AggregateLifecycle.apply(AuctionStartedEvent(command.productId, endedDate, BidStatus.STARTED))
            return "Auction start!!"
        } else {
            throw IllegalStateException("The auction is already started. I can't be auctioned twice nor thrice.")
        }
    }

    @EventSourcingHandler
    fun oc(event: AuctionStartedEvent){
        productId = event.productId
        endedDateTime = event.endedDateTime
        status = event.status
    }

    @DeadlineHandler (deadlineName = AUCTION_DEADLINE)
    fun handleAuction() {
        AggregateLifecycle.apply(AuctionEndedEvent(productId, BidStatus.ENDED))
    }

    @EventSourcingHandler
    fun on(event: AuctionEndedEvent) {
        productId = event.productId
        status = event.status
    }

    @CommandHandler
    fun handle(command: BidProductCommand): String{
        if(status==BidStatus.STARTED){
            if ((command.currentHighestBid > currentHighestBid) &&
                (command.currentHighestBid > startPrice)){
                    AggregateLifecycle.apply(
                        ProductBiddenEvent(
                            command.productId,
                            command.currentBidOwner,
                            command.currentHighestBid
                        )
                    )
                return "You successfully bid a product. You are now the highest!"
            } else if (command.currentHighestBid < startPrice){
                throw IllegalStateException("Sorry, you bid less price than start price.")}
            else {
                throw IllegalStateException(
                    "Sorry but the others has higher bid than you. Now highest bid is " + currentHighestBid + "! Raise for it!")
            }
        } else if (status == BidStatus.ENDED) {
            throw IllegalStateException("Sorry, an auction for this product is already ended a while ago")
        } else { // BidStatus.NONE
            throw IllegalStateException("Sorry, an auction for this product is not started yet")
        }
    }

    @EventSourcingHandler
    fun on(event: ProductBiddenEvent){
        productId = event.productId
        currentBidOwner = event.currentBidOwner
        currentHighestBid = event.currentHighestBid
    }

    fun updateStatus(productId: String){

    }

}