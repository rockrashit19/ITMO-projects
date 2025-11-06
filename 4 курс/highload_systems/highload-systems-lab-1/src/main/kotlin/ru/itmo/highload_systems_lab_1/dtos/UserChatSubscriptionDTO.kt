package ru.itmo.highload_systems_lab_1.dtos

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import ru.itmo.highload_systems_lab_1.utils.UserConstraints

data class CreateUserChatSubscriptionDTO(
    @field:NotBlank(message = "Логин пользователя обязателен")
    @field:Size(min = UserConstraints.LOGIN_MIN, max = UserConstraints.LOGIN_MAX,
        message = "Длина логина не должна быть меньше ${UserConstraints.LOGIN_MIN} или больше ${UserConstraints.LOGIN_MAX} символов")
    val userLogin: String,

    @field:NotNull(message = "ID чата обязателен")
    val chatId: Int
)

data class ReadUserChatSubscriptionDTO(
    val userLogin: String,
    val chatId: Int
)
