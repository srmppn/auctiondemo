package com.example.auctiondemo.aggregate

import com.example.auctiondemo.api.command.*
import com.example.auctiondemo.domain.BidStatus
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.IllegalStateException
import java.math.BigDecimal
import java.time.Instant

class ProductAggregateTest {
    private lateinit var fixture : FixtureConfiguration<ProductAggregate>
    private var random = EasyRandom()
    private var productId : String = "1234"
    private var durationMin: Long = 20
    private var shortDurationMin: Long = 0
    private var startPrice: BigDecimal = BigDecimal(100)
    private var normalBid: BigDecimal = BigDecimal(120)
    private var bidLessThanStart: BigDecimal = BigDecimal(90)
    private var bidGreater: BigDecimal = BigDecimal(140)


    @BeforeEach
    fun setup(){
        fixture = AggregateTestFixture(ProductAggregate:: class.java)
    }

    fun createProductEvent() : ProductCreatedEvent{
        return random.nextObject(ProductCreatedEvent:: class.java)
            .copy(productId = productId, startPrice = startPrice)
    }

    fun startAuctionEvent() : AuctionStartedEvent{
        val endedDate = Instant.now().plusSeconds(durationMin*60)
        return random.nextObject(AuctionStartedEvent:: class.java)
            .copy(productId = productId, status = BidStatus.STARTED, endedDateTime = endedDate)
    }

    fun shortAuctionEvent() : AuctionStartedEvent{
        val endedDate = Instant.now().plusSeconds(shortDurationMin*60)
        return random.nextObject(AuctionStartedEvent:: class.java)
            .copy(productId = productId, status = BidStatus.STARTED, endedDateTime = endedDate)
    }

    @Test
    fun whenCreateProduct_shouldPublishEvent(){
        val command = random.nextObject(CreateProductCommand::class.java)
        fixture.givenNoPriorActivity()
            .`when`(command)
            .expectEvents(
                ProductCreatedEvent(
                    productId = command.productId,
                    name = command.name,
                    description = command.description,
                    startPrice = command.startPrice,
                    status = BidStatus.NONE)
            )
    }

    @Test
    fun whenStartAuction_shouldPublishEventAndEndedDateAndStatusIsUpdated(){
        val startCommand = random.nextObject(StartAuctionCommand:: class.java)
            .copy(productId = productId, durationMin = durationMin)
        fixture.given(createProductEvent())
            .`when`(startCommand)
            .expectEvents(
                AuctionStartedEvent(
                    productId = productId,
                    endedDateTime = Instant.now().plusSeconds(durationMin*60),
                    status = BidStatus.STARTED
                )
            )
        // Error
        // created Date != expected Date, there was a slightly differences in milliseconds
    }

    @Test
    fun whenStartAuctionMultipleTime_shouldThrowError(){
        val startCommand = random.nextObject(StartAuctionCommand:: class.java)
            .copy(productId = productId, durationMin = durationMin)
        fixture.given(createProductEvent(), startAuctionEvent())
            .`when`(startCommand)
            .expectNoEvents()
            .expectExceptionMessage("The auction is already started. I can't be auctioned twice nor thrice.")
    }


    @Test
    fun whenBidProductCorrectly_shouldUpdatedData(){
        val bidCommand = random.nextObject(BidProductCommand:: class.java)
            .copy(productId = productId)
        fixture.given(createProductEvent(), startAuctionEvent())
            .`when`(bidCommand)
            .expectEvents(
                ProductBiddenEvent(
                    productId = productId,
                    currentBidOwner = bidCommand.currentBidOwner,
                    currentHighestBid = bidCommand.currentHighestBid
                )
            )
    }

    @Test
    fun whenBidProductHigherThanProvious_shouldUpdateData(){
        val bidEvent = random.nextObject(ProductBiddenEvent:: class.java)
            .copy(productId = productId, currentHighestBid = normalBid)
        val higherBidCommand = random.nextObject(BidProductCommand:: class.java)
            .copy(productId = productId, currentHighestBid = bidGreater)
        fixture.given(createProductEvent(), startAuctionEvent(), bidEvent)
            .`when`(higherBidCommand)
            .expectEvents(ProductBiddenEvent(
                productId = higherBidCommand.productId,
                currentBidOwner = higherBidCommand.currentBidOwner,
                currentHighestBid = higherBidCommand.currentHighestBid
            ))
    }

    @Test
    fun whenBidProductWithoutStartAuction_shouldThrowError(){
        val bidCommand = random.nextObject(BidProductCommand:: class.java)
            .copy(productId = productId)
        fixture.given(createProductEvent())
            .`when`(bidCommand)
            .expectNoEvents()
            .expectExceptionMessage("Sorry, an auction for this product is not started yet")
    }

    @Test
    fun whenBidProductWithPriceLessThanStart_shouldThrowError(){
        val bidCommand = random.nextObject(BidProductCommand:: class.java)
            .copy(productId = productId, currentHighestBid = bidLessThanStart)
        fixture.given(createProductEvent(), startAuctionEvent())
            .`when`(bidCommand)
            .expectNoEvents()
            .expectExceptionMessage("Sorry, you bid less price than start price.")
    }

    @Test
    fun whenBidProductLessThanPrevious_shouldThrowError(){
        val highBidEvent = random.nextObject(ProductBiddenEvent:: class.java)
            .copy(productId = productId, currentHighestBid = bidGreater)
        val lowBidCommand = random.nextObject(BidProductCommand:: class.java)
            .copy(productId = productId, currentHighestBid = normalBid)
        fixture.given(createProductEvent(), startAuctionEvent(), highBidEvent)
            .`when`(lowBidCommand)
            .expectNoEvents()
            .expectExceptionMessage("Sorry but the others has higher bid than you. Now highest bid is " +
                    highBidEvent.currentHighestBid + "! Raise for it!")
    }

//    @Test
//    fun whenBidProductWhenAuctionIsEnded_shouldThrowError(){
//        val bidCommand = random.nextObject(BidProductCommand:: class.java)
//            .copy(productId = productId, currentHighestBid = normalBid)
//        fixture.given(createProductEvent(), shortAuctionEvent())
//            .`when`(bidCommand)
//            .expectNoEvents()
//    }


}