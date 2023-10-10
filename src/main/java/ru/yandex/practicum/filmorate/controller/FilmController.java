package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.IternalServerException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    private int id = 1;

    @PostMapping
    public ResponseEntity<?> addFilm(@RequestBody Film film) {
        try {
            checkValidity(film);
            film.setId(id);
            films.put(id, film);
            id += 1;
            log.debug("addFilm " + film);
            return ResponseEntity.status(HttpStatus.CREATED).body(film);
        } catch (ValidationException e) {
            String errorMessage = "Ошибка при создании фильма - " + e.getMessage();
            log.info("addFilm " + film + " " + errorMessage);
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateFilm(@RequestBody Film film) {
        try {
            if (!films.containsKey(film.getId())) {
                String errorMessage = "Фильм " + film + " по данному id не найден!\n";
                errorMessage += films + "\n";
                log.info(errorMessage);
                throw new IternalServerException(errorMessage);
            }
            checkValidity(film);
            films.put(film.getId(), film);
            log.debug("updateFilm " + film);
            return ResponseEntity.status(HttpStatus.OK).body(film);
        } catch (ValidationException e) {
            String errorMessage = "Ошибка при обновлении фильма - " + e.getMessage();
            log.info("updateFilm " + film + " " + errorMessage);
            return ResponseEntity.badRequest().body(e);
        } catch (IternalServerException e) {
            String errorMessage = "Фильм не найден - " + e.getMessage();
            log.info("updateFilm " + film + " " + errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.debug("getAllFilms " + films);
        return new ArrayList<>(films.values());
    }

    private void checkValidity(Film film) throws ValidationException {
        String errorMessage = "";
        if(film.getName() == null || film.getName().equals("")) {
            errorMessage += "название не может быть пустым!\n";
        }
        if(film.getDescription().length() > 200) {
            errorMessage += "максимальная длина описания — 200 символов!\n";
        }
        if(LocalDate.of(1895, Month.DECEMBER, 28).isAfter(film.getReleaseDate())) {
            errorMessage += "дата релиза — не раньше 28 декабря 1895 года!\n";
        }
        if(film.getDuration() <= 0L) {
            errorMessage += "продолжительность фильма должна быть положительной!\n";
        }
        if(!errorMessage.equals("")) {
            log.info(errorMessage);
            throw new ValidationException(errorMessage);
        }
    }
}
