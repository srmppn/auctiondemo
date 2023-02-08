package com.example.auctiondemo.configuration

import org.axonframework.common.transaction.TransactionManager
import org.axonframework.deadline.SimpleDeadlineManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AxonConfiguration {
    @Bean
    fun deadlineManager(config: org.axonframework.config.Configuration, transactionManager: TransactionManager) = SimpleDeadlineManager.builder()
        .transactionManager(transactionManager)
        .scopeAwareProvider(config.scopeAwareProvider())
        .build()
}