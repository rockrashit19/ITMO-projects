package ru.itmo.highload_systems_lab_1.database.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import ru.itmo.highload_systems_lab_1.utils.UserConstraints
import ru.itmo.highload_systems_lab_1.utils.Role

@Entity
@Table(name = "users")
class User(
    @field:Id
    @field:NotBlank(message = "Логин обязателен")
    @field:Size(min = UserConstraints.LOGIN_MIN, max = UserConstraints.LOGIN_MAX,
        message = "Длина логина не должна быть меньше ${UserConstraints.LOGIN_MIN} или больше ${UserConstraints.LOGIN_MAX} символов")
    @field:Column(name = "login", length = UserConstraints.LOGIN_MAX, nullable = false)
    var login: String,

    @field:NotBlank(message = "Пароль обязателен")
    @field:Column(name = "password_hash", length = UserConstraints.PASSWORD_HASH_LENGTH, nullable = false)
    var passwordHash: String,

    @field:Enumerated(EnumType.STRING)
    @field:Column(name = "role", length = UserConstraints.ROLE_LENGTH, nullable = false)
    var role: Role = Role.USER,

    @field:Column(name = "invited_count", nullable = false)
    var invitedCount: Int = 0
)