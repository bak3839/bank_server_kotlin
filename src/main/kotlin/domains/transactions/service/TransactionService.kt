package org.example.domains.transactions.service

import org.example.common.cache.RedisClient
import org.example.common.cache.RedisKeyProvider
import org.example.common.exception.CustomException
import org.example.common.exception.ErrorCode
import org.example.common.json.JsonUtil
import org.example.common.logging.Logging
import org.example.common.message.KafkaProducer
import org.example.common.transaction.Transactional
import org.example.domains.transactions.model.DepositResponse
import org.example.domains.transactions.model.TransferResponse
import org.example.domains.transactions.repository.TransactionsAccount
import org.example.domains.transactions.repository.TransactionsUser
import org.example.types.dto.Response
import org.example.types.dto.ResponseProvider
import org.example.types.entity.Account
import org.example.types.entity.User
import org.example.types.message.TransactionMessage
import org.slf4j.Logger
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class TransactionService(
    private val transactionsUser: TransactionsUser,
    private val transactionsAccount: TransactionsAccount,
    private val redisClient: RedisClient,
    private val transactional: Transactional,
    private val logger: Logger = Logging.getLogger(TransactionService::class.java),
    private val producer: KafkaProducer
) {
    fun deposit(userUlid: String, accountUlid: String, value: BigDecimal): Response<DepositResponse> = Logging.logFor(logger) { log ->
        log["userUlid"] = userUlid
        log["accountUlid"] = accountUlid
        log["value"] = value

        val key = RedisKeyProvider.bankMutexKey(userUlid, accountUlid)

        return@logFor redisClient.invokeWithMutex(key) {
            return@invokeWithMutex transactional.run {
                val user = transactionsUser.findByUlid(userUlid)
                val account = transactionsAccount.findByUlidAndUser(accountUlid, user)
                    ?: throw CustomException(ErrorCode.FAILED_TO_FIND_ACCOUNT)

                account.balance = account.balance.add(value)
                account.updatedAt = LocalDateTime.now()
                transactionsAccount.save(account)

                val message = JsonUtil.encodeToJson(TransactionMessage(
                    fromUlid = "0x0",
                    fromName = "0x0",
                    fromAccountID = "0x0",
                    toUlid = userUlid,
                    toName = user.username,
                    toAccountID = accountUlid,
                    value = value,
                    time = LocalDateTime.now(),
                ), TransactionMessage.serializer())

                //producer.sendMessage()

                ResponseProvider.success(DepositResponse(afterBalance = account.balance))
            }
        }
    }

    fun transfer(fromUlid: String, fromAccountUlid: String, toAccountUlid: String, value: BigDecimal): Response<TransferResponse> = Logging.logFor(logger) { log ->

        log["fromUlid"] = fromUlid
        log["fromAccountUlid"] = fromAccountUlid
        log["toAccountUlid"] = toAccountUlid
        log["value"] = value

        val key = RedisKeyProvider.bankMutexKey(fromUlid, fromAccountUlid)

        return@logFor redisClient.invokeWithMutex(key) {
            return@invokeWithMutex transactional.run {
                val fromUser = transactionsUser.findByUlid(fromUlid)
                val fromAccount = transactionsAccount.findByUlidAndUser(fromAccountUlid, fromUser)
                    ?: throw CustomException(ErrorCode.FAILED_TO_FIND_ACCOUNT)



                if(fromAccount.balance < value) {
                    throw CustomException(ErrorCode.ENOUGH_VALUE)
                } else if(value <= BigDecimal.ZERO) {
                    throw CustomException(ErrorCode.VALUE_MUST_NOT_BE_UNDER_ZERO)
                }

                val toAccount = transactionsAccount.findByUlid(toAccountUlid)
                    ?: throw CustomException(ErrorCode.FAILED_TO_FIND_ACCOUNT)

                fromAccount.balance = fromAccount.balance.subtract(value)
                toAccount.balance = toAccount.balance.add(value)

                transactionsAccount.save(fromAccount)
                transactionsAccount.save(toAccount)

                ResponseProvider.success(
                    TransferResponse(
                        afterFromBalance = fromAccount.balance,
                        afterToBalance = toAccount.balance
                    )
                )
            }
        }
    }
}