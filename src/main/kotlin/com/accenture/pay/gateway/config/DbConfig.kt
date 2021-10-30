package com.accenture.pay.gateway.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.support.locks.DefaultLockRegistry
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
class DbConfig {
    @Bean
    fun lockRegistry(): DefaultLockRegistry {
        return DefaultLockRegistry()
    }
}