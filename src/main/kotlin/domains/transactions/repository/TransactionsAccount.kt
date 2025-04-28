package org.example.domains.transactions.repository

import org.example.types.entity.Account
import org.example.types.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionsAccount : JpaRepository<Account, String> {
    fun findByUlidAndUser(ulid: String, user: User): Account?
    fun findByUlid(accountUlid: String): Account?
}