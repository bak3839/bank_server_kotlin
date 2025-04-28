package org.example.domains.transactions.controller

import org.example.domains.transactions.model.DepositRequest
import org.example.domains.transactions.model.DepositResponse
import org.example.domains.transactions.model.TransferRequest
import org.example.domains.transactions.model.TransferResponse
import org.example.domains.transactions.service.TransactionService
import org.example.types.dto.Response
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/transactional")
class TransactionController(
    private val transactionService: TransactionService
) {
    @PostMapping("/deposit")
    fun deposit(@RequestBody(required = true) request: DepositRequest) : Response<DepositResponse> {
        return transactionService.deposit(request.toUlid, request.toAccountUlid, request.value)
    }

    @PostMapping("/transfer")
    fun transfer(@RequestBody(required = true) request: TransferRequest) : Response<TransferResponse> {
        return transactionService.transfer(request.fromUlid, request.fromAccountUlid, request.toAccountUlid, request.value)
    }
}