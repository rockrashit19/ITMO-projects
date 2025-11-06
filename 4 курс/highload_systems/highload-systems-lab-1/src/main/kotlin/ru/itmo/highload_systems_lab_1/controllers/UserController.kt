package ru.itmo.highload_systems_lab_1.controllers

import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.itmo.highload_systems_lab_1.dtos.CreateUserDTO
import ru.itmo.highload_systems_lab_1.dtos.ReadUserDTO
import ru.itmo.highload_systems_lab_1.dtos.UpdateUserDTO
import ru.itmo.highload_systems_lab_1.services.UserService
import ru.itmo.highload_systems_lab_1.utils.API_URL

@RestController
@RequestMapping("$API_URL/user")
class UserController(
    private val userService: UserService
) {
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody @Valid userDTO: CreateUserDTO): ReadUserDTO =
        userService.createUser(userDTO)

    @GetMapping("/{login}")
    fun getUser(@PathVariable login: String): ReadUserDTO =
        userService.getUser(login)

    @PutMapping("/{login}")
    fun updateUser(
        @PathVariable login: String,
        @RequestBody @Valid dto: UpdateUserDTO
    ): ReadUserDTO = userService.updateUser(login, dto)

    @DeleteMapping("/{login}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable login: String) {
        userService.deleteUser(login)
    }

    @GetMapping
    fun getUsers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "50") size: Int
    ): Page<ReadUserDTO> = userService.findAll(page, size)
}
