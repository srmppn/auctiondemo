package com.example.auctiondemo.query

import com.example.auctiondemo.api.query.FetchAllProducts
import com.example.auctiondemo.api.query.FetchProductById
import com.example.auctiondemo.domain.AuctionProduct
import com.example.auctiondemo.repository.AuctionProductRepository
import org.axonframework.queryhandling.QueryHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class AuctionProductQuery {
    @Autowired
    private lateinit var productRepository: AuctionProductRepository

    @QueryHandler
    fun handle (query: FetchAllProducts): CompletableFuture<List<AuctionProduct>> =
        productRepository.findAll()
            .collectList()
            .toFuture()

    @QueryHandler
    fun handle (query: FetchProductById): CompletableFuture<AuctionProduct> {
        return  productRepository.findById(query.productId)
            .toFuture()
    }

}