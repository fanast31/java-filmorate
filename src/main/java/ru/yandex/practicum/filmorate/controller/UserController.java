package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.exeption.IternalServerException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController extends BaseController<User> {

    @PostMapping
    public ResponseEntity<?> addUser(@Valid @RequestBody User user) {
        try {
            validate(user);
            if (user.getName() == null || user.getName().equals("")) {
                user.setName(user.getLogin());
            }
            create(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (ValidationException e) {
            String errorMessage = "Ошибка при создании пользователя - " + e.getMessage();
            log.info("addUser " + user + " " + errorMessage);
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user) {
        try {
            validate(user);
            update(user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (ValidationException e) {
            String errorMessage = "Ошибка при обновлении пользователя - " + e.getMessage();
            log.info("updateFilm " + user + " " + errorMessage);
            return ResponseEntity.badRequest().body(e);
        } catch (IternalServerException e) {
            String errorMessage = "Пользователь не найден - " + e.getMessage();
            log.info("updateFilm " + user + " " + errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.debug("getAllUsers " + getAll());
        return getAll();
    }

    @DeleteMapping()
    public void clearAll() {
        clearAll();
    }

    @Override
    public void validate(User data) throws ValidationException {

    }

}
