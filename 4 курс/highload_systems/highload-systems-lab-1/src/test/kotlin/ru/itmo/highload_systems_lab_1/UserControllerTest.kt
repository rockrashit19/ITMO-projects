package ru.itmo.highload_systems_lab_1.users

import com.fasterxml.jackson.databind.ObjectMapper
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.itmo.highload_systems_lab_1.controllers.UserController
import ru.itmo.highload_systems_lab_1.dtos.CreateUserDTO
import ru.itmo.highload_systems_lab_1.dtos.ReadUserDTO
import ru.itmo.highload_systems_lab_1.services.UserService
import ru.itmo.highload_systems_lab_1.utils.API_URL
import ru.itmo.highload_systems_lab_1.utils.Role
import kotlin.test.Test

@WebMvcTest(UserController::class)
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var userService: UserService

    @Test
    fun `should create user with valid data`() {
        val createUserDTO = CreateUserDTO(
            login = "testuser",
            password = "password123"
        )

        val readUserDTO = ReadUserDTO(
            login = "testuser",
            role = Role.USER,
            invitedCount = 0
        )

        `when`(userService.createUser(createUserDTO))
            .thenReturn(readUserDTO)

        mockMvc.perform(
            MockMvcRequestBuilders.post("$API_URL/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(createUserDTO))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.login").value("testuser"))
    }

    @Test
    fun `should return user by login`() {
        val readUserDTO = ReadUserDTO(
            login = "testuser",
            role = Role.USER,
            invitedCount = 0
        )

        `when`(userService.getUser("testuser"))
            .thenReturn(readUserDTO)

        mockMvc.perform(
            MockMvcRequestBuilders.get("$API_URL/user/testuser")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.login").value("testuser"))
    }

    @Test
    fun `should return paginated users`() {
        val users = listOf(
            ReadUserDTO(login = "user1", role = Role.USER, invitedCount = 0),
            ReadUserDTO(login = "user2", role = Role.USER, invitedCount = 0)
        )
        val page = PageImpl(users, PageRequest.of(0, 50), 2)

        `when`(userService.findAll(0, 50))
            .thenReturn(page)

        mockMvc.perform(
            MockMvcRequestBuilders.get("$API_URL/user")
                .param("page", "0")
                .param("size", "50")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalElements").value(2))
    }
}
