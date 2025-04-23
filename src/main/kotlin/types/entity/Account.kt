package org.example.types.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "account")
data class Account (
    @Id
    @Column(name = "ulid", length = 12, nullable = false)
    val ulid : String,

    @Column(name = "balance", nullable = false, precision = 15, scale = 2)
    var balance : BigDecimal = BigDecimal.ZERO,

    @Column(name = "account_number", length = 100, nullable = false, unique = true)
    val accountNumber: String,

    // 엔티티의 논리적인 삭제 관리
    @Column(name = "is_deleted", nullable = false)
    val isDeleted : Boolean = false,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false, updatable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_ulid", nullable = false)
    val user : User
)