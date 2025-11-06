package ru.itmo.highload_systems_lab_1.controllers

import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.itmo.highload_systems_lab_1.dtos.CreateAccessKeyDTO
import ru.itmo.highload_systems_lab_1.dtos.ReadAccessKeyDTO
import ru.itmo.highload_systems_lab_1.dtos.UpdateAccessKeyDTO
import ru.itmo.highload_systems_lab_1.services.AccessKeyService
import ru.itmo.highload_systems_lab_1.utils.API_URL

@RestController
@RequestMapping("$API_URL/access-key")
class AccessKeyController(
    private val accessKeyService: AccessKeyService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAccessKey(@RequestBody @Valid dto: CreateAccessKeyDTO): ReadAccessKeyDTO =
        accessKeyService.createAccessKey(dto)

    @GetMapping("/{key}")
    fun getAccessKey(@PathVariable key: String): ReadAccessKeyDTO =
        accessKeyService.getAccessKey(key)

    @PutMapping("/{key}")
    fun updateAccessKey(
        @PathVariable key: String,
        @RequestBody @Valid dto: UpdateAccessKeyDTO
    ): ReadAccessKeyDTO = accessKeyService.updateAccessKey(key, dto)

    @DeleteMapping("/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAccessKey(@PathVariable key: String) {
        accessKeyService.deleteAccessKey(key)
    }

    @GetMapping
    fun getAccessKeys(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "50") size: Int
    ): Page<ReadAccessKeyDTO> = accessKeyService.findAll(page, size)
}
