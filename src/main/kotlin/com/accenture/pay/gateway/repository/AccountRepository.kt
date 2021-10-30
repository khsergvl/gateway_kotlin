package com.accenture.pay.gateway.repository

import com.accenture.pay.gateway.entity.Account
import org.springframework.data.repository.CrudRepository

interface AccountRepository : CrudRepository<Account, Int>