package com.example.auctiondemo.aggregate

import com.example.auctiondemo.api.command.*
import com.example.auctiondemo.domain.BidStatus
import com.example.auctiondemo.repository.AuctionProductRepository
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.util.Date

@Aggregate
class ProductAggregate() {

    @AggregateIdentifier
    private lateinit var productId: String
    private lateinit var name: String
    private lateinit var description: String
    private lateinit var startPrice: BigDecimal
    private lateinit var endedDateTime: Date
//    private var endedDateTime: Date
    private lateinit var status: BidStatus
    private lateinit var currentBidOwner: String
    private lateinit var currentHighestBid: BigDecimal

    @Autowired
    private lateinit var productRepository: AuctionProductRepository

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
    }

    @CommandHandler
    fun handle(command : StartAuctionCommand) : String{
        val date = productRepository.findById(command.productId).block()!!.endedDateTime
        if(date == null){
            val endedDate = Date(System.currentTimeMillis() + (command.durationMin * 60 * 1000))
            println(endedDate)
            AggregateLifecycle.apply(StartAuctionEvent(command.productId, endedDate, BidStatus.STARTED))
            return "Auction start!!"
        } else {
            return "The auction is already started. I can't be auctioned twice nor thrice."
        }
    }

    @EventSourcingHandler
    fun oc(event: StartAuctionEvent){
        productId = event.productId
        endedDateTime = event.endedDateTime
        status = BidStatus.STARTED
    }

    @CommandHandler
    fun handle(command: BidProductCommand): String {
        val product = productRepository.findById(command.productId).block()!!
        val date = product.endedDateTime
        if (date == null){
            return "Sorry, an auction for this product is not started yet"
        } else if (date.before(Date())) {
            return "Sorry, an auction for this product is already ended a while ago"
        } else {
            val currentHighestBid = product.currentHighestBid
            if(currentHighestBid==null){
                AggregateLifecycle.apply(BidProductEvent(command.productId,command.currentBidOwner,command.currentHighestBid))
                return "You are the first to bid a product"
            } else if (currentHighestBid>=command.currentHighestBid) {
                return "The others has higher bid than you! Now highest bid is " + currentHighestBid + "!"
            } else {
                AggregateLifecycle.apply(BidProductEvent(command.productId,command.currentBidOwner,command.currentHighestBid))
                return "You are successfully bid a product. You are now the highest!"
            }
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