package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.exeption.IternalServerException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/users")
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    private int id = 1;

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody User user) {
        try {
            checkValidity(user, false);
            if (user.getName() == null || user.getName().equals("")) {
                user.setName(user.getLogin());
            }
            user.setId(id);
            users.put(id, user);
            id += 1;
            log.debug("addUser " + user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (ValidationException e) {
            String errorMessage = "Ошибка при создании пользователя - " + e.getMessage();
            log.info("addUser " + user + " " + errorMessage);
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        try {
            if (!users.containsKey(user.getId())) {
                String errorMessage = "Пользователь " + user + "по данному id не найден!\n";
                errorMessage += users + "\n";
                throw new IternalServerException(errorMessage);
            }
            checkValidity(user, true);
            users.put(user.getId(), user);
            log.debug("updateUser " + user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (ValidationException e) {
            String errorMessage = "Ошибка при обновлении пользователя - " + e.getMessage();
            log.info("updateUser " + user + " " + errorMessage);
            return ResponseEntity.badRequest().body(e);

        } catch (IternalServerException e) {
            String errorMessage = "Пользователь не найден - " + e.getMessage();
            log.info("updateUser " + user + " " + errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.debug("getAllUsers " + users);
        return new ArrayList<>(users.values());
    }

    private void checkValidity(User user, boolean checkId) throws ValidationException {
        String errorMessage = "";
        if(checkId && user.getId() == null) {
            errorMessage += "id не может быть null @!\n";
        }
        if(user.getEmail() == null || user.getEmail().equals("") || !user.getEmail().contains("@")) {
            errorMessage += "электронная почта не может быть пустой и должна содержать символ @!\n";
        }
        if(user.getLogin() == null || user.getLogin().equals("") || user.getLogin().contains(" ")) {
            errorMessage += "логин не может быть пустым и содержать пробелы!\n";
        }
        if(user.getBirthday().isAfter(LocalDate.now())) {
            errorMessage += "дата рождения не может быть в будущем!\n";
        }
        if(!errorMessage.equals("")) {
            log.info(errorMessage);
            throw new ValidationException(errorMessage);
        }
    }
}
