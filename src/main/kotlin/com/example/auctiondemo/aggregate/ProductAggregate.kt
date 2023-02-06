package com.example.auctiondemo.aggregate

import com.example.auctiondemo.api.command.*
import com.example.auctiondemo.command.*
import com.example.auctiondemo.domain.BidStatus
import com.example.auctiondemo.repository.AuctionProductRepository
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import org.springframework.beans.factory.annotation.Autowired
import reactor.kotlin.core.publisher.toMono
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
//            null,
//            null,
//            null,
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
////        println("---------------------------------------------" + command.productId)
////        println("---------------------------------------------" + productRepository.findById(command.productId).block())
////        var productStatus = productRepository.findById(command.productId).block()!!.status
////        println("---------------------------------------------" + (productStatus == BidStatus.NONE))
////        println("---------------------------------------------" + (productStatus.equals(BidStatus.NONE)))
////        if (productStatus == BidStatus.NONE) {
////            val endedDate = Timestamp(System.currentTimeMillis() + (command.durationMin * 60 * 1000))
        val endedDate = Date(System.currentTimeMillis() + (command.durationMin*60*1000))
//        val endedDate = LocalDateTime.now().plusMinutes(command.durationMin)
            AggregateLifecycle.apply(StartAuctionEvent(command.productId, endedDate,  BidStatus.STARTED))
            return "Auction start!!"
//
////        } else if (productStatus == BidStatus.STARTED) {
////            return "Sorry. But the auction has already started at few time ago."
////        }
////        else {
////            return "Sorry. But the auction has already started and ended."
////        }
    }

    @EventSourcingHandler
    fun oc(event: StartAuctionEvent){
        productId = event.productId
        endedDateTime = event.endedDateTime
        status = event.status
    }

    @CommandHandler
    fun handle(command: BidProductCommand): String {
        AggregateLifecycle.apply(BidProductEvent(command.productId, command.currentBidOwner, command.currentHighestBid))
        return "You bid a product"
    }

    @EventSourcingHandler
    fun on(event: BidProductEvent){
        productId = event.productId
        currentBidOwner = event.currentBidOwner
        currentHighestBid = event.currentHighestBid
    }

}