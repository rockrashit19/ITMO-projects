package ru.itmo.highload_systems_lab_1.services

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ru.itmo.highload_systems_lab_1.database.repositories.ChatRepository
import ru.itmo.highload_systems_lab_1.dtos.CreateChatDTO
import ru.itmo.highload_systems_lab_1.dtos.ReadChatDTO
import ru.itmo.highload_systems_lab_1.dtos.UpdateChatDTO
import ru.itmo.highload_systems_lab_1.mappers.ChatMapper
import ru.itmo.highload_systems_lab_1.exceptions.ChatNotFoundException
import ru.itmo.highload_systems_lab_1.services.util.pageRequest

@Service
class ChatService(
    private val chatRepository: ChatRepository,
    private val chatMapper: ChatMapper
) {

    fun createChat(dto: CreateChatDTO): ReadChatDTO {
        val entity = chatMapper.toEntity(dto)
        val saved = chatRepository.save(entity)
        return chatMapper.toReadDTO(saved)
    }

    fun getChat(id: Int): ReadChatDTO {
        val chat = chatRepository.findById(id).orElseThrow { ChatNotFoundException(id) }
        return chatMapper.toReadDTO(chat)
    }

    fun updateChat(id: Int, dto: UpdateChatDTO): ReadChatDTO {
        val chat = chatRepository.findById(id).orElseThrow { ChatNotFoundException(id) }
        chatMapper.toEntity(dto, chat)
        val saved = chatRepository.save(chat)
        return chatMapper.toReadDTO(saved)
    }

    fun deleteChat(id: Int) {
        if (!chatRepository.existsById(id)) {
            throw ChatNotFoundException(id)
        }
        chatRepository.deleteById(id)
    }

    fun findAll(page: Int, size: Int): Page<ReadChatDTO> {
        val pageable = pageRequest(page, size)
        return chatRepository.findAll(pageable).map { chatMapper.toReadDTO(it) }
    }
}
