package ru.itmo.highload_systems_lab_1.dtos

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import ru.itmo.highload_systems_lab_1.utils.UserConstraints

data class CreateUserProjectDTO(
    @field:NotBlank(message = "Логин пользователя обязателен")
    @field:Size(min = UserConstraints.LOGIN_MIN, max = UserConstraints.LOGIN_MAX,
        message = "Длина логина не должна быть меньше ${UserConstraints.LOGIN_MIN} или больше ${UserConstraints.LOGIN_MAX} символов")
    val userLogin: String,

    @field:NotNull(message = "ID проекта обязателен")
    val projectId: Int,

    @field:Email(message = "Некорректный формат email")
    @field:Size(max = 64, message = "Email не должен превышать 64 символа")
    val notificationEmail: String? = null
)

data class UpdateUserProjectDTO(
    @field:Email(message = "Некорректный формат email")
    @field:Size(max = 64, message = "Email не должен превышать 64 символа")
    val notificationEmail: String? = null
)

data class ReadUserProjectDTO(
    val userLogin: String,
    val projectId: Int,
    val notificationEmail: String?
)
