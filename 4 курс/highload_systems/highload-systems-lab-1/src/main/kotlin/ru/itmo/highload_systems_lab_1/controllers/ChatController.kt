package ru.itmo.highload_systems_lab_1.controllers

import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.itmo.highload_systems_lab_1.dtos.CreateChatDTO
import ru.itmo.highload_systems_lab_1.dtos.ReadChatDTO
import ru.itmo.highload_systems_lab_1.dtos.UpdateChatDTO
import ru.itmo.highload_systems_lab_1.services.ChatService
import ru.itmo.highload_systems_lab_1.utils.API_URL

@RestController
@RequestMapping("$API_URL/chat")
class ChatController(
    private val chatService: ChatService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createChat(@RequestBody @Valid dto: CreateChatDTO): ReadChatDTO =
        chatService.createChat(dto)

    @GetMapping("/{id}")
    fun getChat(@PathVariable id: Int): ReadChatDTO =
        chatService.getChat(id)

    @PutMapping("/{id}")
    fun updateChat(
        @PathVariable id: Int,
        @RequestBody @Valid dto: UpdateChatDTO
    ): ReadChatDTO = chatService.updateChat(id, dto)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteChat(@PathVariable id: Int) {
        chatService.deleteChat(id)
    }

    @GetMapping
    fun getChats(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "50") size: Int
    ): Page<ReadChatDTO> = chatService.findAll(page, size)
}
