package ru.itmo.highload_systems_lab_1.database.entities

import jakarta.persistence.*
import ru.itmo.highload_systems_lab_1.utils.UserConstraints


@Entity
@Table(name = "user_chat_subscriptions")
data class UserChatSubscription(
    @field:EmbeddedId
    var id: UserChatSubscriptionId
)

@Embeddable
data class UserChatSubscriptionId(
    @field:Column(name = "user_login", length = UserConstraints.LOGIN_MAX, nullable = false)
    var userLogin: String,

    @field:Column(name = "chat_id", nullable = false)
    var chatId: Int
) : java.io.Serializable