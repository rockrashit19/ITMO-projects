package ru.itmo.highload_systems_lab_1.controllers

import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.itmo.highload_systems_lab_1.dtos.CreateFileDTO
import ru.itmo.highload_systems_lab_1.dtos.ReadFileDTO
import ru.itmo.highload_systems_lab_1.dtos.UpdateFileDTO
import ru.itmo.highload_systems_lab_1.services.FileService
import ru.itmo.highload_systems_lab_1.utils.API_URL

@RestController
@RequestMapping("$API_URL/file")
class FileController(
    private val fileService: FileService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createFile(@RequestBody @Valid dto: CreateFileDTO): ReadFileDTO =
        fileService.createFile(dto)

    @GetMapping("/{id}")
    fun getFile(@PathVariable id: Int): ReadFileDTO =
        fileService.getFile(id)

    @PutMapping("/{id}")
    fun updateFile(
        @PathVariable id: Int,
        @RequestBody @Valid dto: UpdateFileDTO
    ): ReadFileDTO = fileService.updateFile(id, dto)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteFile(@PathVariable id: Int) {
        fileService.deleteFile(id)
    }

    @GetMapping
    fun getFiles(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "50") size: Int
    ): Page<ReadFileDTO> = fileService.findAll(page, size)
}
