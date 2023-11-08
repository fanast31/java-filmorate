package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user));
    }

    @PutMapping()
    public ResponseEntity<?> update(@Valid @RequestBody User user) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.update(user));
        } catch (DataNotFoundException e) {
            String errorMessage = "Пользователь не найден - " + e.getMessage();
            log.info("updateFilm " + user + " " + errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @GetMapping
    public List<User> getAll() {
        log.debug("getAllUsers " + userService.getAll());
        return userService.getAll();
    }

    @DeleteMapping()
    public ResponseEntity<?> clearAll() {
        userService.clearAll();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
