package org.example.domains.bank.service

import com.github.f4b6a3.ulid.UlidCreator
import org.example.common.exception.CustomException
import org.example.common.exception.ErrorCode
import org.example.common.logging.Logging
import org.example.common.transaction.Transactional
import org.example.domains.bank.repository.BankAccountRepository
import org.example.domains.bank.repository.BankUserRepository
import org.example.types.dto.Response
import org.example.types.dto.ResponseProvider
import org.example.types.entity.Account
import org.slf4j.Logger
import org.springframework.stereotype.Service
import java.lang.Math.random
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class BankService(
    private val transactional: Transactional,
    private val bankUserRepository: BankUserRepository,
    private val bankAccountRepository: BankAccountRepository,
    private val logger: Logger = Logging.getLogger(BankService::class.java)
) {
    fun createAccount(userUlid: String): Response<String> = Logging.logFor(logger) { log ->
        log["userUlid"] = userUlid
        println(userUlid)

        transactional.run {
            val user = bankUserRepository.findByUlid(userUlid)

            val ulid = UlidCreator.getUlid().toString()
            val accountNumber = generateRandomAccountNumber()

            val account = Account(
                ulid = ulid,
                user = user,
                accountNumber = accountNumber
            )

            try {
                bankAccountRepository.save(account)
            } catch (e: Exception) {
                throw CustomException(ErrorCode.FAILED_TO_SAVE_DATA, e.message)
            }
        }

        return@logFor ResponseProvider.success("SUCCESS")
    }

    fun balance(userUlid: String, accountUlid: String): Response<BigDecimal> = Logging.logFor(logger) { log ->
        log["userUlid"] = userUlid
        log["accountUlid"] = accountUlid

        return@logFor transactional.run {
            val account = bankAccountRepository.findByUlid(accountUlid)
                ?: throw CustomException(ErrorCode.FAILED_TO_FIND_ACCOUNT)

            if(account.user.ulid != userUlid) throw CustomException(ErrorCode.MISS_MATCH_ACCOUNT_ULID_AND_UESR_ULID)
            ResponseProvider.success(account.balance)
        }
    }

    // TODO -> 동시성 처리 고려
    fun removeAccount(userUlid: String, accountUlid: String): Response<String> = Logging.logFor(logger) { log ->
        log["userUlid"] = userUlid
        log["accountUlid"] = accountUlid

        return@logFor transactional.run {
            val account = bankAccountRepository.findByUlid(accountUlid)
                ?: throw CustomException(ErrorCode.FAILED_TO_FIND_ACCOUNT)

            if(account.user.ulid != userUlid) throw CustomException(ErrorCode.MISS_MATCH_ACCOUNT_ULID_AND_UESR_ULID)
            if(account.balance.compareTo(BigDecimal.ZERO) != 0) throw CustomException(ErrorCode.ACCOUNT_IS_NOT_ZERO)

            val updatedAccount = account.copy(
                isDeleted = true,
                deletedAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )

            bankAccountRepository.save(updatedAccount)
            ResponseProvider.success("SUCCESS")
        }
    }

    private fun generateRandomAccountNumber(): String {
        val bankCode = "003"
        val section = "12"

        val number = random().toString()
        return "$bankCode-$section$-$number"
    }
}