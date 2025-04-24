package org.example.common.logging

import org.example.common.exception.CustomException
import org.example.common.exception.ErrorCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/*
    AOP
    특정 로직이나, 함수가 시작되기 전,후에 처리가 가능한 기능
    @PointCut
    - 복잡성
    - 컴파일 시 문제가 발견 안됨
 */

object Logging {
    fun <T: Any> getLogger(clazz: Class<T>): Logger = LoggerFactory.getLogger(clazz)

    fun <T> logFor(log: Logger, function: (MutableMap<String, Any>) -> T?) : T {
        val logInfo = mutableMapOf<String, Any>()
        logInfo["start_at"] = now()

        val result = function.invoke(logInfo)

        logInfo["end_at"] = now()

        log.info(logInfo.toString())

        return result ?: throw CustomException(ErrorCode.FAILED_TO_INVOKE_IN_LOGGER)
    }

    private fun now(): Long {
        return System.currentTimeMillis()
    }
}