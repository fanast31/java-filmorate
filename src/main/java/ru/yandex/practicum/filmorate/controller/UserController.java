package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@PathVariable Long id, @PathVariable Long friendId) throws DataNotFoundException {
        userService.addFriend(id, friendId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable Long id, @PathVariable Long friendId) throws DataNotFoundException {
        userService.removeFriend(id, friendId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<?> getFriends(@PathVariable Long id) throws DataNotFoundException {
        List<User> filmsSet = userService.getFriends(id);
        return ResponseEntity.status(HttpStatus.OK).body(filmsSet);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<?> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) throws DataNotFoundException {
        // Логика получения списка общих друзей
        List<User> filmsSet = userService.getCommonFriends(id, otherId);
        return ResponseEntity.status(HttpStatus.OK).body(filmsSet);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) throws DataNotFoundException {
        User user = userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user));
    }

    @PutMapping()
    public ResponseEntity<?> update(@Valid @RequestBody User user) throws DataNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(user));
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @DeleteMapping()
    public ResponseEntity<?> clearAll() {
        userService.clearAll();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
