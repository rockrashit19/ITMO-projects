package ru.itmo.highload_systems_lab_1.dtos

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import ru.itmo.highload_systems_lab_1.utils.AccessKeyConstraints
import java.time.LocalDateTime

data class CreateAccessKeyDTO(
    @field:Size(min = AccessKeyConstraints.KEY_LENGTH, max = AccessKeyConstraints.KEY_LENGTH,
        message = "Длина ключа должна быть ровно ${AccessKeyConstraints.KEY_LENGTH} символов")
    val key: String,

    @field:NotNull(message = "Логин пользователя обязателен")
    val userLogin: String,

    @field:NotNull(message = "Дата окончания срока действия обязательна")
    val lifespanEndDate: LocalDateTime,

    val usagesLimit: Int? = null
)

data class UpdateAccessKeyDTO(
    val lifespanEndDate: LocalDateTime?,

    val usagesLimit: Int? = null
)

data class ReadAccessKeyDTO(
    val key: String,
    val userLogin: String,
    val lifespanEndDate: LocalDateTime,
    val usagesLimit: Int?,
    val usagesCount: Int
)
