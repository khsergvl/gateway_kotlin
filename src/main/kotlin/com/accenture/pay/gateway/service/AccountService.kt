package com.accenture.pay.gateway.service

import com.accenture.pay.gateway.entity.Account
import com.accenture.pay.gateway.model.Status

interface AccountService {
    fun getAccount(id: Int): Account
    fun transfer(from: Int, to: Int, amount: Double, runnable: Runnable?): Status
}