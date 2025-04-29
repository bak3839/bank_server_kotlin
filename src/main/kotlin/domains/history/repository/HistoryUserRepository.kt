package org.example.domains.history.repository

import com.github.f4b6a3.ulid.Ulid
import org.example.types.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface HistoryUserRepository: JpaRepository<User, String> {
    fun findByUlid(ulid: String): User
}