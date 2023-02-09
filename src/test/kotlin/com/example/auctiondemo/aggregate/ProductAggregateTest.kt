package com.example.auctiondemo.aggregate

import com.example.auctiondemo.api.command.*
import com.example.auctiondemo.domain.BidStatus
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant

class ProductAggregateTest {
    private lateinit var fixture : FixtureConfiguration<ProductAggregate>
    private val random = EasyRandom()
    private var productId : String = "1234"
    private var durationMin: Long = 20


    @BeforeEach
    fun setup(){
        fixture = AggregateTestFixture(ProductAggregate:: class.java)
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
        fixture.given(createEvent)
            .`when`(startCommand)
            .expectEvents(
                StartAuctionEvent(
                    productId = productId,
                    endedDateTime = Instant.now().plusSeconds(durationMin*60),
                    status = BidStatus.STARTED
                )
            )
        // Error
        // created Date != expected Date, there was a slightly differences in milliseconds
    }


    @Test
    fun whenBidProductCorrectly_ShouldUpdatedData(){
        val createEvent = random.nextObject(CreateProductEvent:: class.java)
            .copy(productId = productId)
        val startEvent = random.nextObject(StartAuctionEvent:: class.java)
            .copy(productId = productId, status = BidStatus.STARTED)
        val bidCommand = random.nextObject(BidProductCommand:: class.java)
            .copy(productId = productId)
        fixture.given(createEvent, startEvent)
            .`when`(bidCommand)
            .expectEvents(
                BidProductEvent(
                    productId = productId,
                    currentBidOwner = bidCommand.currentBidOwner,
                    currentHighestBid = bidCommand.currentHighestBid
                )
            )
    }

}