package com.accenture.pay.gateway.controller

import com.accenture.pay.gateway.exception.UnprocessableStateException
import com.accenture.pay.gateway.model.Payment
import com.accenture.pay.gateway.model.Status
import com.accenture.pay.gateway.service.AccountService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@Validated
class PaymentController(private val accountService: AccountService) {

    @PostMapping(
        value = ["/transfer"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun transfer(@Valid @RequestBody payment: Payment): ResponseEntity<Status> {
        //why kotlin why???
        if (payment.amount!! < 0.01) throw UnprocessableStateException("The minimum transfer value is 0.01")
        val transferStatus = accountService.transfer(payment.from!!, payment.to!!, payment.amount!!, null)
        val httpsStatus = if (transferStatus == Status.SUCCESS) HttpStatus.OK else HttpStatus.BAD_REQUEST
        return ResponseEntity(transferStatus, httpsStatus)
    }
}