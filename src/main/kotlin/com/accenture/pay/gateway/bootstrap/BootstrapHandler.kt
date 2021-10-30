package com.accenture.pay.gateway.bootstrap

import com.accenture.pay.gateway.entity.Account
import com.accenture.pay.gateway.repository.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

/**
 * This class designed to pre-initialize application
 * with test data to simplify manual testing
 */
@Component
class BootstrapHandler(val accountRepository: AccountRepository) : ApplicationListener<ApplicationStartedEvent> {

    override fun onApplicationEvent(event: ApplicationStartedEvent) {
        accountRepository.saveAll(listOf(Account(1, 0.15), Account(2, 0.20)))
    }
}