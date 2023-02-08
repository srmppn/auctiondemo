package com.example.auctiondemo

import com.google.type.DateTime
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.util.Date

@SpringBootApplication
class AuctiondemoApplication

fun main(args: Array<String>) {
	runApplication<AuctiondemoApplication>(*args)

//	val instant = Date().toInstant()
//	println(instant)
}
