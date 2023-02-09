package com.example.auctiondemo.projection

import com.example.auctiondemo.api.command.AuctionEndedEvent
import com.example.auctiondemo.api.command.ProductBiddenEvent
import com.example.auctiondemo.api.command.ProductCreatedEvent
import com.example.auctiondemo.api.command.AuctionStartedEvent
import com.example.auctiondemo.domain.AuctionProduct
import com.example.auctiondemo.domain.BidStatus
import com.example.auctiondemo.repository.AuctionProductRepository
import org.axonframework.eventhandling.EventHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ProductProjection {
    @Autowired
    private lateinit var productRepository: AuctionProductRepository

    @EventHandler
    fun on(event: ProductCreatedEvent){
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
    fun on(event: AuctionStartedEvent){
        productRepository.findById(event.productId)
            .map { it.copy( status = BidStatus.STARTED, endedDateTime = event.endedDateTime)}
            .flatMap { productRepository.save(it) }
            .block()
    }

    @EventHandler
    fun on (event: ProductBiddenEvent){
        productRepository.findById(event.productId)
            .map { it.copy( currentBidOwner = event.currentBidOwner, currentHighestBid = event.currentHighestBid) }
            .flatMap { productRepository.save(it) }
            .block()
    }

    @EventHandler
    fun on (event: AuctionEndedEvent){
        productRepository.findById(event.productId)
            .map { it.copy( status = event.status) }
            .flatMap { productRepository.save(it) }
            .block()
    }

}