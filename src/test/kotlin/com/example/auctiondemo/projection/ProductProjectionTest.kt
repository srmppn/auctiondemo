package com.example.auctiondemo.projection

import com.example.auctiondemo.api.command.CreateProductEvent
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

}