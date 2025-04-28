package org.example.domains.transactions.model

import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal

data class DepositRequest(
    @field:NotBlank(message = "enter to account id")
    val toAccountUlid: String,

    @field:NotBlank(message = "enter to ulid")
    val toUlid: String,

    @field:NotBlank(message = "enter value")
    val value: BigDecimal,
)

data class TransferRequest(
    @field:NotBlank(message = "enter to account id")
    val toAccountUlid: String,

    @field:NotBlank(message = "enter from account id")
    val fromAccountUlid: String,

    @field:NotBlank(message = "enter from ulid")
    val fromUlid: String,

    @field:NotBlank(message = "enter value")
    val value: BigDecimal,
)