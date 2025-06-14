package org.example.common.cache

import kotlinx.serialization.KSerializer
import org.example.common.exception.CustomException
import org.example.common.exception.ErrorCode
import org.redisson.api.RedissonClient
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisClient(
    private val template: RedisTemplate<String, String>,
    private val redissonClient: RedissonClient
) {
    fun get(key: String): String? {
        return template.opsForValue().get(key)
    }

    // 타입을 받아서 값이 반드시 존재하는 경우에 대해서 반환
    fun <T> get(key: String, kSerializer: (Any) -> T?): T? {
        val value = get(key)
        value?.let {
            return kSerializer(it)
        } ?: return null
    }

    fun setIfNotExist(key: String, value: String): Boolean {
        // 해당하는 키의 값이 없는 경우 생성
        // Absent -> 없는 경우
        // Present -> 있는 경우
        return template.opsForValue().setIfAbsent(key, value) ?: false
    }

    fun <T> invokeWithMutex(key: String, function: () -> T?) : T? {
        val lock = redissonClient.getLock(key)
        var lockAcquired = false

        try {
            lockAcquired = lock.tryLock(10, 15, TimeUnit.SECONDS)

            if(!lockAcquired) {
                throw CustomException(ErrorCode.FILED_TO_GET_LOCK, key)
            }

            return function.invoke()
        } catch (e: Exception) {
            throw CustomException(ErrorCode.FAILED_TO_MUTEX_INVOKE, e.message)
        } finally {
            if(lockAcquired && lock.isHeldByCurrentThread) {
                lock.unlock()
            }
        }
    }
}