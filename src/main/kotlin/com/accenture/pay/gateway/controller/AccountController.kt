package com.accenture.pay.gateway.controller

import com.accenture.pay.gateway.entity.Account
import com.accenture.pay.gateway.service.AccountService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountController(private val accountService: AccountService) {
    @GetMapping("/account/{id}")
    fun getAccount(@PathVariable id: Int): ResponseEntity<Account> {
        val account = accountService.getAccount(id)
        return ResponseEntity(account, HttpStatus.OK)
    }
}