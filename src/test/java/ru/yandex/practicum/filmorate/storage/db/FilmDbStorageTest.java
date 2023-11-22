package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindUserById() {

        Film newFilm = Film.builder()
                .id(1L)
                .name("userRows.getString()")
                .description("description")
                .releaseDate(LocalDate.of(1990, 1, 1))
                .duration(1)
                .mpa(new MPA(1L))
                .build();
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);
        filmDbStorage.create(newFilm);

        Film newFilm2 = filmDbStorage.findById(1L);

        assertThat(newFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm2);
    }

    @Test
    public void testUpdate() {

        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);

        Film newFilm = Film.builder()
                .name("userRows.getString()")
                .description("description")
                .releaseDate(LocalDate.of(1990, 1, 1))
                .duration(1)
                .mpa(new MPA(1L))
                .build();
        filmDbStorage.create(newFilm);

        newFilm.setName("123");
        newFilm.setMpa(new MPA(2L));
        filmDbStorage.update(newFilm);

        Film newFilm2 = filmDbStorage.findById(1L);

        assertThat(newFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm2);
    }

    @Test
    public void testGetAll() {

        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);

        Film newFilm = Film.builder()
                .name("userRows.getString()")
                .description("description")
                .releaseDate(LocalDate.of(1990, 1, 1))
                .duration(1)
                .mpa(new MPA(1L))
                .build();
        filmDbStorage.create(newFilm);

        Film newFilm2 = Film.builder()
                .name("userRows.getString()111")
                .description("description1111")
                .releaseDate(LocalDate.of(1990, 2, 1))
                .duration(2)
                .mpa(new MPA(2L))
                .build();
        filmDbStorage.create(newFilm2);

        List<Film> list = filmDbStorage.getAll();
        List<Film> list2 = new ArrayList<>();
        list2.add(newFilm);
        list2.add(newFilm2);

        assertThat(list)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(list2);
    }

}