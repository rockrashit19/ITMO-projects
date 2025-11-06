package ru.itmo.highload_systems_lab_1.mappers

import org.mapstruct.*
import ru.itmo.highload_systems_lab_1.database.entities.UserChatSubscription
import ru.itmo.highload_systems_lab_1.database.entities.UserChatSubscriptionId
import ru.itmo.highload_systems_lab_1.dtos.*

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
abstract class UserChatSubscriptionMapper {

    @Mapping(target = "id", source = ".", qualifiedByName = ["createUserChatSubscriptionId"])
    abstract fun toEntity(createUserChatSubscriptionDTO: CreateUserChatSubscriptionDTO): UserChatSubscription

    @Mapping(target = "userLogin", source = "id.userLogin")
    @Mapping(target = "chatId", source = "id.chatId")
    abstract fun toReadDTO(userChatSubscription: UserChatSubscription): ReadUserChatSubscriptionDTO

    @Named("createUserChatSubscriptionId")
    fun createUserChatSubscriptionId(dto: CreateUserChatSubscriptionDTO): UserChatSubscriptionId {
        return UserChatSubscriptionId(dto.userLogin, dto.chatId)
    }
}
