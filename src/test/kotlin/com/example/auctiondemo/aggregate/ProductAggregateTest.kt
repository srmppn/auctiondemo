package com.example.auctiondemo.aggregate

import com.example.auctiondemo.api.command.*
import com.example.auctiondemo.domain.BidStatus
import com.example.auctiondemo.dto.CreateProductRequest
import com.example.auctiondemo.repository.AuctionProductRepository
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.axonframework.test.aggregate.TestExecutor
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.Instant
import java.util.*

class ProductAggregateTest {
    private lateinit var fixture : FixtureConfiguration<ProductAggregate>
    private val random = EasyRandom()
    private var productId : String = "1234"
    private var name = "name"
    private var description = "desc"
    private var startPrice = BigDecimal(200)
    private var durationMin: Long = 20

    @Autowired
    private lateinit var auctionProductRepository: AuctionProductRepository

    @BeforeEach
    fun setup(){
        fixture = AggregateTestFixture(ProductAggregate:: class.java)
    }

    fun createProduct(): TestExecutor<ProductAggregate>{
        val product1 = random.nextObject(CreateProductCommand::class.java)
            .copy(productId = productId, name = name, description = description, startPrice = startPrice)
        val product2 = random.nextObject(CreateProductEvent::class.java)
            .copy(productId = productId, name = name, description = description, startPrice = startPrice)
        println(product1)
        println(product2)
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
        val createEvent = random.nextObject(CreateProductEvent:: class.java)
            .copy(productId = productId)
        val startCommand = random.nextObject(StartAuctionCommand:: class.java)
            .copy(productId = productId, durationMin = durationMin)
        println(productId)
        println(createEvent)
        println(startCommand.toString())
        fixture.given(createEvent)
            .`when`(startCommand)
            .expectNoEvents()
            .expectEvents(
                StartAuctionEvent(
                    productId = productId,
                    endedDateTime = Instant.now().plusSeconds(durationMin*60),
                    status = BidStatus.STARTED
                )
            )
        // Error
        // One of error is since the created Date != expected Date, there was a slightly differences in milliseconds
    }


    @Test
    fun whenBidProductCorrectly_ShouldUpdatedData(){
        val createEvent = random.nextObject(CreateProductEvent:: class.java)
            .copy(productId = productId)
        val startEvent = random.nextObject(StartAuctionEvent:: class.java)
            .copy(productId = productId, status = BidStatus.STARTED)
        val bidCommand = random.nextObject(BidProductCommand:: class.java)
            .copy(productId = productId)
        println(createEvent)
        println(startEvent)
        println(bidCommand)
        fixture.given(createEvent, startEvent)
            .`when`(bidCommand)
//            .expectNoEvents()
            .expectEvents(
                BidProductEvent(
                    productId = productId,
                    currentBidOwner = bidCommand.currentBidOwner,
                    currentHighestBid = bidCommand.currentHighestBid
                )
            )

        // Error
        // Both StartAuctionCommand and BidProductCommand are including GET data from repository which has to retrieve from DB
            // The test one didn't save the data really. So it is the cause of error.
    }

}