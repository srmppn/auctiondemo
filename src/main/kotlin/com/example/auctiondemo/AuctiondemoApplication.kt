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

//	val d3 = LocalDateTime.now().plusMinutes(20)
//	println(d3)
//
//	val d4 = Instant.now().plusMillis(20*60*1000)
//	println(d4)


//	val d = Date(System.currentTimeMillis() + (60*60*1000))
//	println(d)

//	val d2 = Timestamp(System.currentTimeMillis() + (60*60*1000))
//	println(d2)

}
