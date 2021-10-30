package com.accenture.pay.gateway.entity

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Account {
    constructor()
    constructor(id: Int?, balance: Double?) {
        this.id = id
        this.balance = balance
    }

    @Id
    @Column
    var id: Int? = null

    @Column
    var balance: Double? = null

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val account = o as Account
        return id == account.id &&
                balance == account.balance
    }

    override fun hashCode(): Int {
        return Objects.hash(id, balance)
    }
}