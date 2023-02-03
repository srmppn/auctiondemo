package com.example.auctiondemo.projection

import com.example.auctiondemo.command.CreateProductEvent
import com.example.auctiondemo.domain.AuctionProduct
import com.example.auctiondemo.repository.AuctionProductRepository
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryUpdateEmitter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.stereotype.Component

@Component
@EnableAutoConfiguration
class ProductProjection {
    @Autowired
    private lateinit var auctionProductRepository: AuctionProductRepository

//    @Autowired
//    private lateinit var queryUpdateEmitter: QueryUpdateEmitter


    @EventHandler
    fun oc(event: CreateProductEvent){
        val auctionProduct = AuctionProduct(event.productId,
            event.name,
            event.description,
            event.startPrice,
            null,
            null,
            null,
            event.status)
        auctionProductRepository.save(auctionProduct).block()
    }

}