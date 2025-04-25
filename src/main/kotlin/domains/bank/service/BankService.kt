package org.example.domains.bank.service

import org.example.common.logging.Logging
import org.example.common.transaction.Transactional
import org.example.domains.bank.repository.BankAccountRepository
import org.example.domains.bank.repository.BankUserRepository
import org.slf4j.Logger
import org.springframework.stereotype.Service

@Service
class BankService(
    private val transactional: Transactional,
    private val userRepository: BankUserRepository,
    private val bankAccountRepository: BankAccountRepository,
    private val logger: Logger = Logging.getLogger(BankService::class.java)
) {
    fun createAccount(userUlid: String) = Logging.logFor(logger) { log ->
        log["userUlid"] = userUlid

        transactional.run {

        }
    }

    fun balance(userUlid: String, accountUlid: String) = Logging.logFor(logger) { log ->
        log["userUlid"] = userUlid
        log["accountUlid"] = accountUlid

        transactional.run {

        }
    }

    // 동시성 이슈
    fun removeAccount(userUlid: String, accountUlid: String) = Logging.logFor(logger) { log ->
        log["userUlid"] = userUlid
        log["accountUlid"] = accountUlid

        transactional.run {

        }
    }
}