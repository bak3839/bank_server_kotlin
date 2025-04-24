package org.example.domains.auth.repository

import org.example.types.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AuthUserRepository : JpaRepository<User, String> {
    fun existsUserByUsername(username: String): Boolean

    @Modifying
    @Query("update User set accessToken = :token where username = :username")
    fun updateAccessTokenByUsername(
        @Param("username") username: String,
        @Param("accessToken") token: String
    )
}