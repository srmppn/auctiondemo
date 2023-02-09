package com.example.auctiondemo.controller

import com.example.auctiondemo.api.command.BidProductCommand
import com.example.auctiondemo.api.command.CreateProductCommand
import com.example.auctiondemo.api.command.StartAuctionCommand
import com.example.auctiondemo.api.query.FetchAllProducts
import com.example.auctiondemo.api.query.FetchProductById
import com.example.auctiondemo.domain.AuctionProduct
import com.example.auctiondemo.dto.BidProductRequest
import com.example.auctiondemo.dto.CreateProductRequest
import com.example.auctiondemo.dto.StartAuctionRequest
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CompletableFuture

@RestController
class ProductController {
    @Autowired
    private lateinit var commandGateway: CommandGateway

    @Autowired
    private lateinit var queryGateway: QueryGateway

    @PostMapping("/product")
    fun createProduct(@RequestBody request: CreateProductRequest): CompletableFuture<String> {
        return commandGateway.send<String>(
            CreateProductCommand(ObjectId.get().toHexString(),
            request.name, request.description, request.startPrice)
        )
    }

    @PutMapping("/product/{productId}/start-auction")
    fun startAuction(@PathVariable("productId") productId: String, @RequestBody request: StartAuctionRequest): CompletableFuture<String> {
        return commandGateway.send<String>(StartAuctionCommand(productId, request.durationMin))
    }

    @PutMapping("/product/{productId}/bid")
    fun bidProduct(@PathVariable("productId") productId: String, @RequestBody request: BidProductRequest): CompletableFuture<String> {
        return commandGateway.send<String>(BidProductCommand(productId, request.name, request.bidAmount))
    }

    @GetMapping("/products")
    fun getAllProducts(): CompletableFuture<List<AuctionProduct>> {
        return queryGateway.query(FetchAllProducts(), ResponseTypes.multipleInstancesOf(AuctionProduct:: class.java))
    }

    @GetMapping("/product/{productId}")
    fun getProductById(@PathVariable("productId") productId: String): CompletableFuture<AuctionProduct> {
        return queryGateway.query(FetchProductById(productId), ResponseTypes.instanceOf(AuctionProduct:: class.java))
    }



}