package com.example.auctiondemo.aggregate

import com.example.auctiondemo.api.command.CreateProductCommand
import com.example.auctiondemo.api.command.CreateProductEvent
import com.example.auctiondemo.api.command.StartAuctionCommand
import com.example.auctiondemo.api.command.StartAuctionEvent
import com.example.auctiondemo.domain.BidStatus
import com.example.auctiondemo.repository.AuctionProductRepository
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.axonframework.test.aggregate.TestExecutor
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.util.*

class ProductAggregateTest {
    private lateinit var fixture : FixtureConfiguration<ProductAggregate>
    private val random = EasyRandom()
    private var productId : String = "1234"
    private var durationMin: Long = 20

    @Autowired
    private lateinit var auctionProductRepository: AuctionProductRepository

    @BeforeEach
    fun setup(){
        fixture = AggregateTestFixture(ProductAggregate:: class.java)
    }

    fun createProduct(): TestExecutor<ProductAggregate>{
        val product1 = random.nextObject(CreateProductCommand::class.java)
            .copy(productId = productId)
        val product2 = random.nextObject(CreateProductCommand::class.java)
            .copy(productId = productId)
        return fixture.given(product1, product2)
    }

    @Test
    fun whenCreateProduct_shouldPublishEvent(){
        val command = random.nextObject(CreateProductCommand::class.java)
        fixture.givenNoPriorActivity()
            .`when`(command)
            .expectEvents(
                CreateProductEvent(
                    productId = command.productId,
                    name = command.name,
                    description = command.description,
                    startPrice = command.startPrice,
                    status = BidStatus.NONE)
            )
    }

    @Test
    fun whenStartAuction_shouldPublishEventAndEndedDateAndStatusIsUpdated(){
        val command1 = random.nextObject(CreateProductCommand:: class.java)
        val command = random.nextObject(StartAuctionCommand:: class.java)
            .copy(productId = productId, durationMin = durationMin)
        println(random)
        println(productId)
        println(command.toString())
        createProduct()
            .`when`(command)
            .expectEvents(
                StartAuctionEvent(
                    productId = command.productId,
                    endedDateTime = Date(System.currentTimeMillis() + (durationMin*60*1000)),
                    status = BidStatus.STARTED)
            )
//            .expectState{
//                assert( it. )
//            }
    }


    @Test
    fun whenBidProductCorrectly_ShouldUpdatedData(){

    }

}