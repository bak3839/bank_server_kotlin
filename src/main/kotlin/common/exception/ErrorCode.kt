package org.example.common.exception


interface CodeInterface {
    val code: Int
    var message: String
}

enum class ErrorCode(
    override val code: Int,
    override var message: String
): CodeInterface {
    AUTH_CONFIG_NOT_FOUND(-100, "Auth config not found"),
    FAILED_TO_CALL_CLIENT(-101, "Failed to call client"),
    CALL_RESULT_BODY_NULL(-102, "Body is nil"),
    PROVIDER_NOT_FOUND(-103, "Provider not found"),
    TOKEN_IS_INVALID(-104, "Token invalid"),
    TOKEN_IS_EXPIRED(-105, "Token expired"),
    FAILED_TO_INVOKE_IN_LOGGER(-106, "Failed to invoke in logger"),
    FAILED_TO_SAVE_DATA(-107, "Failed to save data"),
    FAILED_TO_FIND_ACCOUNT(-108, "Failed to find account"),
    MISS_MATCH_ACCOUNT_ULID_AND_UESR_ULID(-109, "Miss match ulid"),
    ACCOUNT_IS_NOT_ZERO(110, "Account is not zero"),
    FAILED_TO_MUTEX_INVOKE(111, "Failed to mutex invoke"),
    FILED_TO_GET_LOCK(112, "Filed get lock"),
    ENOUGH_VALUE(113, "Enough value"),
    VALUE_MUST_NOT_BE_UNDER_ZERO(114, "Value must be under zero"),
    FAILED_TO_SEND_MESSAGE(115, "Failed to send message"),
}