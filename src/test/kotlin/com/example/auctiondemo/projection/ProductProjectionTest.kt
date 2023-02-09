package com.example.auctiondemo.projection

import com.example.auctiondemo.api.command.BidProductEvent
import com.example.auctiondemo.api.command.CreateProductEvent
import com.example.auctiondemo.api.command.StartAuctionEvent
import com.example.auctiondemo.domain.BidStatus
import com.example.auctiondemo.repository.AuctionProductRepository
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ProductProjectionTest {

    @Autowired
    private lateinit var productRepository: AuctionProductRepository
    @Autowired
    private lateinit var productProjection: ProductProjection

    val random = EasyRandom()
    @Test
    fun whenCreateProduct_addDataToDB(){
        val productEvent = random.nextObject(CreateProductEvent:: class.java)
        productProjection.on(productEvent)
        val result = productRepository.findById(productEvent.productId).block()!!
        assert(result.productId == productEvent.productId)
        assert(result.name == productEvent.name)
        assert(result.description == productEvent.description)
        assert(result.startPrice == productEvent.startPrice)
    }

    @Test
    fun whenStartAuction_editDataInDB(){
        val startAuctionEvent = random.nextObject(StartAuctionEvent:: class.java)
            .copy(status = BidStatus.STARTED)
        productProjection.on(startAuctionEvent)
        val result = productRepository.findById(startAuctionEvent.productId).block()!!
        assert(result.productId == startAuctionEvent.productId)
        assert(result.endedDateTime == startAuctionEvent.endedDateTime)
        assert(result.status == startAuctionEvent.status)
    }

    @Test
    fun whenBidProduct_editDataInDB(){
        val bidProductEvent = random.nextObject(BidProductEvent:: class.java)
        productProjection.on(bidProductEvent)
        val result = productRepository.findById(bidProductEvent.productId).block()!!
        assert(result.productId == bidProductEvent.productId)
        assert(result.currentBidOwner == bidProductEvent.currentBidOwner)
        assert(result.currentHighestBid == bidProductEvent.currentHighestBid)
    }

}