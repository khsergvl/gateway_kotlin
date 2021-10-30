package com.accenture.pay.gateway.model

import javax.validation.constraints.DecimalMin

class Payment {
    var from: Int? = null
    var to: Int? = null
    @field:DecimalMin(value = "0.01", message = "The minimum transfer value is 0.01") var amount: Double? = null
}
