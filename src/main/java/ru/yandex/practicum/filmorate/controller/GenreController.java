package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class GenreController {

    private final FilmService filmService;

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> likeFilm(@PathVariable Long id, @PathVariable Long userId) throws DataNotFoundException {
        filmService.addLike(id, userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> unlikeFilm(@PathVariable Long id, @PathVariable Long userId) throws DataNotFoundException {
        filmService.addDislike(id, userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getTopFilms(@RequestParam(required = false, defaultValue = "10") int count) {
        List<Film> filmsList = filmService.getTopFilms(count);
        return ResponseEntity.status(HttpStatus.OK).body(filmsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilm(@PathVariable Long id) throws DataNotFoundException {
        Film film = filmService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(film);
    }

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        Film newFilm = filmService.create(film);
        return ResponseEntity.status(HttpStatus.CREATED).body(newFilm);
    }

    @PutMapping()
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) throws DataNotFoundException {
        Film newFilm = filmService.update(film);
        return ResponseEntity.status(HttpStatus.OK).body(newFilm);
    }

    @DeleteMapping()
    public ResponseEntity<Void> clearAll() {
        filmService.clearAll();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(filmService.getAll());
    }

}
