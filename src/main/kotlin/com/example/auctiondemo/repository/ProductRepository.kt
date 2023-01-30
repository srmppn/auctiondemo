package com.example.auctiondemo.repository

import com.example.auctiondemo.domain.AuctionProduct
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface ProductRepository: ReactiveCrudRepository<AuctionProduct, String> {
}