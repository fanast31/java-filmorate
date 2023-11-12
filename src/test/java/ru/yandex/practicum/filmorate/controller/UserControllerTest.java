package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {
    private static final String PATH = "/users";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;
    private User user;
    private String json;
    private MvcResult result;
    private String responseJson;
    private User responseUser;

    private User newUser() {
        return User.builder()
                .id(1L)
                .email("fff@gmail.com")
                .login("login")
                .birthday(LocalDate.of(2000, 10, 10))
                .name("name")
                .build();
    }

    private String newJson(User newUser) {
        try {
            return objectMapper.writeValueAsString(newUser);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        user = newUser();
        json = newJson(user);
    }

    @Test
    void create_Positive() throws Exception {

        result =
                mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(MockMvcResultMatchers.status().is(201))
                        .andReturn();

        responseJson = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        responseUser = objectMapper.readValue(responseJson, User.class);

        assertEquals(user, responseUser);

    }

    @Test
    void create_Negative_email() throws Exception {

        user.setEmail("dddd");
        json = newJson(user);
        result =
                mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andReturn();

        user.setEmail(" ");
        json = newJson(user);
        result =
                mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andReturn();
    }

    @Test
    void create_Negative_login() throws Exception {

        user.setLogin("  ");
        json = newJson(user);
        result =
                mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andReturn();

    }

    @Test
    void create_Name() throws Exception {

        user.setName("");
        json = newJson(user);
        result =
                mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(MockMvcResultMatchers.status().is(201))
                        .andReturn();
        responseJson = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        responseUser = objectMapper.readValue(responseJson, User.class);
        assertEquals(user.getLogin(), responseUser.getName());

    }

    @Test
    void create_Negative_birthday() throws Exception {

        user.setBirthday(LocalDate.of(2025,12,27));
        json = newJson(user);
        result =
                mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andReturn();

    }

    @Test
    void update_Positive() throws Exception {

        result =
                mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(MockMvcResultMatchers.status().is(201))
                        .andReturn();
        responseJson = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        responseUser = objectMapper.readValue(responseJson, User.class);
        responseUser.setName("update");

        json = newJson(responseUser);
        result =
                mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();
        responseJson = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        User responseUser2 = objectMapper.readValue(responseJson, User.class);

        assertEquals(responseUser, responseUser2);
        assertEquals(responseUser.getName(), responseUser2.getName());

    }

    @Test
    void update_Negative() throws Exception {

        result =
                mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(MockMvcResultMatchers.status().is(201))
                        .andReturn();
        responseJson = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        responseUser = objectMapper.readValue(responseJson, User.class);
        responseUser.setId(15L);

        json = newJson(responseUser);
        result =
                mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(MockMvcResultMatchers.status().isNotFound())
                        .andReturn();

    }

    @Test
    void getAll_Empty() throws Exception {

        result =
                mockMvc.perform(MockMvcRequestBuilders.get(PATH))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();
        responseJson = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

        ArrayList<User> list = objectMapper.readValue(responseJson, new TypeReference<ArrayList<User>>() {});
        assertEquals(userService.getAll(), list);
    }

    @Test
    void getAll_NotEmpty() throws Exception {

        userService.create(user);
        result =
                mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(MockMvcResultMatchers.status().is(201))
                        .andReturn();
        result =
                mockMvc.perform(MockMvcRequestBuilders.get(PATH))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();
        responseJson = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

        ArrayList<User> list = objectMapper.readValue(responseJson, new TypeReference<ArrayList<User>>() {});
        assertEquals(userService.getAll(), list);

    }

}