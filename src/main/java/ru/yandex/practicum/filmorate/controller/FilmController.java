package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.IternalServerException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/films")
public class FilmController extends BaseController<Film> {

    @PostMapping
    public ResponseEntity<?> addFilm(@Valid @RequestBody Film film) {
        try {
            create(film);
            return ResponseEntity.status(HttpStatus.CREATED).body(film);
        } catch (ValidationException e) {
            String errorMessage = "Ошибка при создании фильма - " + e.getMessage();
            log.info("addFilm " + film + " " + errorMessage);
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateFilm(@Valid @RequestBody Film film) {
        try {
            update(film);
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

    @DeleteMapping()
    public void clearAll() {
        super.clearAll();
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.debug("getAllFilms " + getAll());
        return getAll();
    }

    @Override
    public void validate(Film data) {

    }

}
