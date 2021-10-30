package com.accenture.pay.gateway.controller

import com.accenture.pay.gateway.entity.Account
import com.accenture.pay.gateway.repository.AccountRepository
import com.accenture.pay.gateway.service.AccountService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest
@WebAppConfiguration
class PaymentControllerTest(@Autowired val accountRepository: AccountRepository) {
    private val firstAccount = Account(3, 0.5)
    private val secondAccount = Account(4, 0.25)

    private var mockMvc: MockMvc? = null

    @BeforeEach
    fun setUp(@Autowired accountService: AccountService) {
        mockMvc = MockMvcBuilders.standaloneSetup(PaymentController(accountService)).build()
    }

    @Test
    @Throws(Exception::class)
    fun happyPath() {
        val amount = 0.2
        assertTrue(amount < firstAccount.balance!!)
        accountRepository.saveAll(listOf(firstAccount, secondAccount))
        val requestBody = "{\"from\":${firstAccount.id},\"to\":${secondAccount.id}, \"amount\":$amount}"
        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/transfer").contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$").value("SUCCESS"))
    }

    @Test
    @Throws(Exception::class)
    fun invalidAmount() {
        val amount = 0.002
        accountRepository.saveAll(listOf(firstAccount, secondAccount))
        val requestBody = "{\"from\":${firstAccount.id},\"to\":${secondAccount.id}, \"amount\":$amount}"
        mockMvc!!.perform(
            (MockMvcRequestBuilders.post("/transfer").contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist())
    }

    @Test
    @Throws(Exception::class)
    fun notEnoughFunds() {
        val amount = 0.6
        assertTrue(amount > (firstAccount.balance)!!)
        accountRepository.saveAll(listOf(firstAccount, secondAccount))
        val requestBody = "{\"from\":${firstAccount.id},\"to\":${secondAccount.id}, \"amount\":$amount}"
        mockMvc!!.perform(
            (MockMvcRequestBuilders.post("/transfer").contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$").value("FAILED"))
    }

    @Test
    @Throws(Exception::class)
    fun accountNotFound() {
        val amount = 0.01
        assertTrue(amount < (firstAccount.balance)!!)
        accountRepository.save(firstAccount)
        assertTrue(!accountRepository.findById((secondAccount.id)!!).isPresent)
        val requestBody = "{\"from\":${firstAccount.id},\"to\":${secondAccount.id}, \"amount\":$amount}"
        mockMvc!!.perform(
            (MockMvcRequestBuilders.post("/transfer").contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist())
    }

    @AfterEach
    fun tearDown() {
        accountRepository.deleteAll()
    }
}