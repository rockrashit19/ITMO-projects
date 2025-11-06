package ru.itmo.highload_systems_lab_1.controllers

import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.itmo.highload_systems_lab_1.dtos.CreateUserChatSubscriptionDTO
import ru.itmo.highload_systems_lab_1.dtos.ReadUserChatSubscriptionDTO
import ru.itmo.highload_systems_lab_1.services.UserChatSubscriptionService
import ru.itmo.highload_systems_lab_1.utils.API_URL

@RestController
@RequestMapping("$API_URL/user-chat-subscription")
class UserChatSubscriptionController(
    private val userChatSubscriptionService: UserChatSubscriptionService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUserChatSubscription(@RequestBody @Valid dto: CreateUserChatSubscriptionDTO): ReadUserChatSubscriptionDTO =
        userChatSubscriptionService.createUserChatSubscription(dto)

    @GetMapping("/{userLogin}/{chatId}")
    fun getUserChatSubscription(
        @PathVariable userLogin: String,
        @PathVariable chatId: Int
    ): ReadUserChatSubscriptionDTO = userChatSubscriptionService.getUserChatSubscription(userLogin, chatId)

    @DeleteMapping("/{userLogin}/{chatId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUserChatSubscription(
        @PathVariable userLogin: String,
        @PathVariable chatId: Int
    ) {
        userChatSubscriptionService.deleteUserChatSubscription(userLogin, chatId)
    }

    @GetMapping
    fun getUserChatSubscriptions(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "50") size: Int
    ): Page<ReadUserChatSubscriptionDTO> = userChatSubscriptionService.findAll(page, size)
}
