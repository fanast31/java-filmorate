package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Film film) {
        filmService.create(film);
        return ResponseEntity.status(HttpStatus.CREATED).body(film);
    }

    @PutMapping()
    public ResponseEntity<?> update(@Valid @RequestBody Film film) {
        try {
            filmService.update(film);
            return ResponseEntity.status(HttpStatus.OK).body(film);
        } catch (DataNotFoundException e) {
            String errorMessage = "Фильм не найден - " + e.getMessage();
            log.info("updateFilm " + film + " " + errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @DeleteMapping()
    public ResponseEntity<?> clearAll() {
        filmService.clearAll();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        log.debug("getAllFilms " + filmService.getAll());
        return ResponseEntity.status(HttpStatus.OK).body(filmService.getAll());
    }

}
