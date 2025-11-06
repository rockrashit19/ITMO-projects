package ru.itmo.highload_systems_lab_1.dtos

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import ru.itmo.highload_systems_lab_1.utils.UserConstraints
import ru.itmo.highload_systems_lab_1.utils.Role

data class CreateUserDTO(
    @field:NotBlank(message = "Логин обязателен")
    @field:Size(min = UserConstraints.LOGIN_MIN, max = UserConstraints.LOGIN_MAX,
                message = "Длина логина не должна быть меньше ${UserConstraints.LOGIN_MIN} или больше ${UserConstraints.LOGIN_MAX} символов")
    val login: String,

    @field:NotBlank(message = "Пароль обязателен")
    val password: String
)

data class UpdateUserDTO(
    @field:NotBlank(message = "Пароль обязателен")
    val password: String
)

data class ReadUserDTO(
    val login: String,
    val role: Role,
    val invitedCount: Int
)
