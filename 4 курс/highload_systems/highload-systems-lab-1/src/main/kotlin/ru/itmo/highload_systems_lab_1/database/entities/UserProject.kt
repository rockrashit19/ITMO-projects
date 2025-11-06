package ru.itmo.highload_systems_lab_1.database.entities

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size
import ru.itmo.highload_systems_lab_1.utils.UserConstraints

@Entity
@Table(name = "user_projects")
data class UserProject(
    @field:EmbeddedId
    var id: UserProjectId,

    @field:Email(message = "Некорректный формат email")
    @field:Size(max = 64, message = "Email не должен превышать 64 символа")
    @field:Column(name = "notification_email", length = 64, nullable = true)
    var notificationEmail: String? = null
)

@Embeddable
data class UserProjectId(
    @field:Column(name = "user_login", length = UserConstraints.LOGIN_MAX, nullable = false)
    var userLogin: String,

    @field:Column(name = "project_id", nullable = false)
    var projectId: Int
) : java.io.Serializable
