package ru.itmo.highload_systems_lab_1.services

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ru.itmo.highload_systems_lab_1.database.entities.UserChatSubscriptionId
import ru.itmo.highload_systems_lab_1.database.repositories.UserChatSubscriptionRepository
import ru.itmo.highload_systems_lab_1.dtos.CreateUserChatSubscriptionDTO
import ru.itmo.highload_systems_lab_1.dtos.ReadUserChatSubscriptionDTO
import ru.itmo.highload_systems_lab_1.mappers.UserChatSubscriptionMapper
import ru.itmo.highload_systems_lab_1.exceptions.UserChatSubscriptionNotFoundException
import ru.itmo.highload_systems_lab_1.services.util.pageRequest

@Service
class UserChatSubscriptionService(
    private val userChatSubscriptionRepository: UserChatSubscriptionRepository,
    private val userChatSubscriptionMapper: UserChatSubscriptionMapper
) {

    fun createUserChatSubscription(dto: CreateUserChatSubscriptionDTO): ReadUserChatSubscriptionDTO {
        val entity = userChatSubscriptionMapper.toEntity(dto)
        val saved = userChatSubscriptionRepository.save(entity)
        return userChatSubscriptionMapper.toReadDTO(saved)
    }

    fun getUserChatSubscription(userLogin: String, chatId: Int): ReadUserChatSubscriptionDTO {
        val id = UserChatSubscriptionId(userLogin, chatId)
        val userChatSubscription = userChatSubscriptionRepository.findById(id).orElseThrow { 
            UserChatSubscriptionNotFoundException(userLogin, chatId) 
        }
        return userChatSubscriptionMapper.toReadDTO(userChatSubscription)
    }

    fun deleteUserChatSubscription(userLogin: String, chatId: Int) {
        val id = UserChatSubscriptionId(userLogin, chatId)
        if (!userChatSubscriptionRepository.existsById(id)) {
            throw UserChatSubscriptionNotFoundException(userLogin, chatId)
        }
        userChatSubscriptionRepository.deleteById(id)
    }

    fun findAll(page: Int, size: Int): Page<ReadUserChatSubscriptionDTO> {
        val pageable = pageRequest(page, size)
        return userChatSubscriptionRepository.findAll(pageable).map { userChatSubscriptionMapper.toReadDTO(it) }
    }
}
