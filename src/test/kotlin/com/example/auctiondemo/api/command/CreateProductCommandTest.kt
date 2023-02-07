package com.example.auctiondemo.api.command

import com.example.auctiondemo.aggregate.ProductAggregate
import com.example.auctiondemo.domain.AuctionProduct
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.saga.FixtureConfiguration
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateProductCommandTest {
//    private  lateinit var fixture : FixtureConfiguration<ProductAggregate>
    private val random = EasyRandom()

    @BeforeEach
    fun setup() {
//        fixture = AggregateTestFixture(ProductAggregate::class.java)
    }

    @Test
    fun whenCreateProduct_shouldPublishEvent(){
//        val command = random.



    }


}