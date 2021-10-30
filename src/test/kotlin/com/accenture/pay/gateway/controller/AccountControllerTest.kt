package com.accenture.pay.gateway.controller

import com.accenture.pay.gateway.entity.Account
import com.accenture.pay.gateway.repository.AccountRepository
import com.accenture.pay.gateway.service.AccountService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
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
class AccountControllerTest(@Autowired val accountRepository: AccountRepository) {
    private var mockMvc: MockMvc? = null

    @BeforeEach
    fun setUp(@Autowired accountService: AccountService) {
        mockMvc = MockMvcBuilders.standaloneSetup(AccountController(accountService)).build()
    }

    @Test
    @Throws(Exception::class)
    fun accountExists() {
        val account = Account(1, 0.5)
        accountRepository.save(account)
        mockMvc!!.perform(MockMvcRequestBuilders.get("/account/${account.id}"))
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(account.id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(account.balance))
    }

    @Test
    @Throws(Exception::class)
    fun accountNoExists() {
        val id = 1
        assertTrue(!accountRepository.findById(id).isPresent)
        mockMvc!!.perform(MockMvcRequestBuilders.get("/account/$id"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @AfterEach
    fun tearDown() {
        accountRepository.deleteAll()
    }

}