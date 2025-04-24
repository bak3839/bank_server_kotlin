package org.example.types.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user")
data class User(
    @Id
    @Column(name = "ulid", length = 26)
    val ulid: String,

    @Column(name = "username", nullable = false, unique = true, length = 50)
    val username: String,

    @Column(name = "access_token", length = 255)
    val accessToken: String? = null,

    @Column(name = "updated_at", nullable = false, updatable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "user")
    val accounts : List<Account> = mutableListOf()
)