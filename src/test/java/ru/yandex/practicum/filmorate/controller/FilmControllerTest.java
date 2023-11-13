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
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmControllerTest {
    private static final String PATH = "/films";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FilmService filmService;
    private Film film;
    private String json;
    private MvcResult result;
    private String responseJson;
    private Film responseFilm;

    private Film newFilm() {
        return Film.builder()
                .id(1L)
                .name("Название фильма")
                .description("Описание фильма")
                .releaseDate(LocalDate.of(2023, 10, 10))
                .duration(120)
                .build();
    }

    private String newJson(Film newFilm) {
        try {
            return objectMapper.writeValueAsString(newFilm);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        film = newFilm();
        json = newJson(film);
        mockMvc.perform(MockMvcRequestBuilders.delete(PATH))
                .andExpect(MockMvcResultMatchers.status().is(200));

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
        responseFilm = objectMapper.readValue(responseJson, Film.class);

        assertEquals(film, responseFilm);

    }

    @Test
    void create_Negative_Description() throws Exception {

        film.setDescription("Далеко-далеко за словесными горами в стране гласных и согласных живут рыбные тексты. " +
                "Вдали от всех живут они в буквенных домах на берегу Семантика " +
                "большого языкового океана. Маленький ручеек Даль журчит ");
        json = newJson(film);
        result =
                mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andReturn();

    }

    @Test
    void create_Negative_Duration() throws Exception {

        film.setDuration(0);
        json = newJson(film);
        result =
                mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andReturn();

    }

    @Test
    void create_Negative_Name() throws Exception {

        film.setName(" ");
        json = newJson(film);
        result =
                mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andReturn();

    }

    @Test
    void create_Negative_releaseDate() throws Exception {

        film.setReleaseDate(LocalDate.of(1895,12,27));
        json = newJson(film);
        result =
                mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andReturn();


        film = newFilm();
        film.setReleaseDate(LocalDate.of(1895,12,29));
        json = newJson(film);
        result =
                mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(MockMvcResultMatchers.status().is(201))
                        .andReturn();
        responseJson = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        responseFilm = objectMapper.readValue(responseJson, Film.class);
        film.setId(responseFilm.getId());
        assertEquals(film, responseFilm);
        assertEquals(film.getReleaseDate(), responseFilm.getReleaseDate());


        film = newFilm();
        film.setReleaseDate(null);
        json = newJson(film);
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
        responseFilm = objectMapper.readValue(responseJson, Film.class);
        responseFilm.setDescription("update");

        json = newJson(responseFilm);
        result =
                mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();
        responseJson = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Film responseFilm2 = objectMapper.readValue(responseJson, Film.class);

        assertEquals(responseFilm, responseFilm2);
        assertEquals(responseFilm.getDescription(), responseFilm2.getDescription());

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
        responseFilm = objectMapper.readValue(responseJson, Film.class);
        responseFilm.setId(15L);

        json = newJson(responseFilm);
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

        ArrayList<Film> list = objectMapper.readValue(responseJson, new TypeReference<ArrayList<Film>>() {});
        assertEquals(filmService.getAll(), list);
    }

    @Test
    void getAll_NotEmpty() throws Exception {

        filmService.create(film);
        film.setId(1L);
        json = newJson(film);

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

        ArrayList<Film> list = objectMapper.readValue(responseJson, new TypeReference<ArrayList<Film>>() {});
        assertEquals(filmService.getAll(), list);

    }

}