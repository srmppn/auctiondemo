package com.example.auctiondemo.projection

import com.example.auctiondemo.command.BidProductEvent
import com.example.auctiondemo.command.CreateProductEvent
import com.example.auctiondemo.command.StartAuctionEvent
import com.example.auctiondemo.domain.AuctionProduct
import com.example.auctiondemo.repository.AuctionProductRepository
import org.axonframework.eventhandling.EventHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.stereotype.Component

@Component
@EnableAutoConfiguration
class ProductProjection {
    @Autowired
    private lateinit var productRepository: AuctionProductRepository

//    @Autowired
//    private lateinit var queryUpdateEmitter: QueryUpdateEmitter


    @EventHandler
    fun on(event: CreateProductEvent){
        val auctionProduct = AuctionProduct(
            event.productId,
            event.name,
            event.description,
            event.startPrice,
            null,
            null,
            null,
            event.status)
        productRepository.save(auctionProduct).block()
    }

    @EventHandler
    fun on(event: StartAuctionEvent){
        productRepository.findById(event.productId)
            .map { it.copy( status = event.status, endedDateTime = event.endedDateTime)}
            .flatMap { productRepository.save(it) }
            .block()
    }

    @EventHandler
    fun on (event: BidProductEvent){
        productRepository.findById(event.productId)
            .map { it.copy( currentBidOwner = event.currentBidOwner, currentHighestBid = event.currentHighestBid) }
            .flatMap { productRepository.save(it) }
            .block()
    }

}