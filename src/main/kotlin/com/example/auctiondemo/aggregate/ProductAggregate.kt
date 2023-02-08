package com.example.auctiondemo.aggregate

import com.example.auctiondemo.api.command.*
import com.example.auctiondemo.domain.BidStatus
import com.example.auctiondemo.repository.AuctionProductRepository
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.deadline.DeadlineManager
import org.axonframework.deadline.annotation.DeadlineHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.lang.IllegalStateException
import java.math.BigDecimal
import java.time.Instant
import java.util.Date

@Aggregate
class ProductAggregate() {

    companion object {
        const val AUCTION_DEADLINE = "auction_deadline"
    }

    @AggregateIdentifier
    private lateinit var productId: String
    private lateinit var name: String
    private lateinit var description: String
    private lateinit var startPrice: BigDecimal
    private lateinit var endedDateTime: Instant
//    private var endedDateTime: Date
    private lateinit var status: BidStatus
    private lateinit var currentBidOwner: String
    private lateinit var currentHighestBid: BigDecimal

    private val SECOND_IN_MINUTE = 60L

    @CommandHandler
    constructor(command: CreateProductCommand) : this(){
        AggregateLifecycle.apply(
            CreateProductEvent(
            command.productId,
            command.name,
            command.description,
            command.startPrice,
            BidStatus.NONE)
        )
    }

    @EventSourcingHandler
    fun on (event: CreateProductEvent){
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
            AggregateLifecycle.apply(StartAuctionEvent(command.productId, endedDate, BidStatus.STARTED))
            return "Auction start!!"
        } else {
            throw IllegalStateException("The auction is already started. I can't be auctioned twice nor thrice.")
//            return "The auction is already started. I can't be auctioned twice nor thrice."
        }
    }

    @EventSourcingHandler
    fun oc(event: StartAuctionEvent){
        productId = event.productId
        endedDateTime = event.endedDateTime
        status = event.status
    }

    @DeadlineHandler
    fun handleAuction() {
        AggregateLifecycle.apply(AuctionEndedEvent(productId, BidStatus.ENDED))
    }

    @EventSourcingHandler
    fun on(event: AuctionEndedEvent) {
        productId = event.productId
        status = event.status
    }

    @CommandHandler
    fun handle(command: BidProductCommand): String {
        println(productId + " " + status)
//        val product = productRepository.findById(command.productId).block()!!
//        val date = product.endedDateTime
//        if (date == null){
//            return "Sorry, an auction for this product is not started yet"
//        } else if (date.isBefore(Instant.now())) {
//            return "Sorry, an auction for this product is already ended a while ago"
//        } else {
//            val currentHighestBid = product.currentHighestBid
//            if(currentHighestBid==null){
//                AggregateLifecycle.apply(BidProductEvent(command.productId,command.currentBidOwner,command.currentHighestBid))
//                return "You are the first to bid a product"
//            } else if (currentHighestBid>=command.currentHighestBid) {
//                return "The others has higher bid than you! Now highest bid is " + currentHighestBid + "!"
//            } else {
//                AggregateLifecycle.apply(BidProductEvent(command.productId,command.currentBidOwner,command.currentHighestBid))
//                return "You are successfully bid a product. You are now the highest!"
//            }
//        }
        if (status == BidStatus.STARTED) {
            if(currentHighestBid==null){
                AggregateLifecycle.apply(BidProductEvent(command.productId,command.currentBidOwner,command.currentHighestBid))
                return "You are the first to bid a product"
            } else if (currentHighestBid>=command.currentHighestBid) {
                return "Sorry. The others has higher bid than you! Now highest bid is " + currentHighestBid + "!"
            } else {
                AggregateLifecycle.apply(BidProductEvent(command.productId,command.currentBidOwner,command.currentHighestBid))
                return "You has successfully bid a product. You are now the highest!"
                }
        } else if (status == BidStatus.ENDED) {
            throw IllegalStateException("Sorry, an auction for this product is already ended a while ago")
        } else {
            throw IllegalStateException("Sorry, an auction for this product is not started yet")
        }
    }

    @EventSourcingHandler
    fun on(event: BidProductEvent){
        productId = event.productId
        currentBidOwner = event.currentBidOwner
        currentHighestBid = event.currentHighestBid
    }

    fun updateStatus(productId: String){

    }

}