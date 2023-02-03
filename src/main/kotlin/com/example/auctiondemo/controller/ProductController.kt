package com.example.auctiondemo.controller

import com.example.auctiondemo.command.CreateProductCommand
import com.example.auctiondemo.command.StartAuctionCommand
import com.example.auctiondemo.domain.AuctionProduct
import com.example.auctiondemo.domain.BidStatus
import com.example.auctiondemo.dto.BidProductRequest
import com.example.auctiondemo.dto.CreateProductRequest
import com.example.auctiondemo.dto.StartAuctionRequest
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Flow.Publisher
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@RestController
class ProductController {
    @Autowired
    private lateinit var commandGateway: CommandGateway

    @Autowired
    private lateinit var queryGateway: QueryGateway

    @PostMapping("/product")
    fun createProduct(@RequestBody request: CreateProductRequest): CompletableFuture<String> {
        return commandGateway.send<String>(CreateProductCommand(ObjectId.get().toHexString(),
            request.name, request.description, request.startPrice))
    }

    @PutMapping("/product/{productId}/start-auction")
    fun startAuction(@PathVariable("productId") productId: String, @RequestBody request: StartAuctionRequest): CompletableFuture<String> {
        return commandGateway.send<String>(StartAuctionCommand(productId, request.durationMin))
    }

//    @PutMapping("/product/{productId}/bid")
//    fun bidProduct(@PathVariable("productId") productId: String, @RequestBody request: BidProductRequest): CompletableFuture<String> {
//        return
//    }
//
//    @GetMapping("/products")
//    fun getAllProducts(): Publisher<List<AuctionProduct>> {
//        return
//    }
//
//    @GetMapping("/product/{productId}")
//    fun getProductById(): Publisher<AuctionProduct> {
//        return
//    }



}