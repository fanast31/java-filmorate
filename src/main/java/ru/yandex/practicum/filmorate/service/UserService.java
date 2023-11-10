package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.memory.InMemoryUserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService extends AbstractService<User>{

    private final InMemoryUserStorage userStorage;

    public void addFriend(Long id1, Long id2) throws DataNotFoundException {
        User user1 = findById(id1);
        User user2 = findById(id2);
        user1.getFriends().add(id2);
        user2.getFriends().add(id1);
    }

    public void removeFriend(Long id1, Long id2) throws DataNotFoundException {
        User user1 = findById(id1);
        User user2 = findById(id2);
        user1.getFriends().remove(id2);
        user2.getFriends().remove(id1);
    }

    public Set<Long> getFriends(Long userId) throws DataNotFoundException {
        User user1 = findById(userId);
        return user1.getFriends();
    }

    public Set<Long> getCommonFriends(Long id1, Long id2) throws DataNotFoundException {

        User user1 = findById(id1);
        User user2 = findById(id2);

        Set<Long> commonFriends = new HashSet<>(user1.getFriends());
        commonFriends.retainAll(user2.getFriends());
        return commonFriends;

    }

    @Override
    public User findById(Long id) throws DataNotFoundException {
        User user = userStorage.findById(id);
        if (user == null) {
            throw new DataNotFoundException("Пользователь с id = " + id + " не найден");
        }
        return user;
    }

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        return userStorage.create(user);
    }

    @Override
    public User update(User user) throws DataNotFoundException {
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        return userStorage.update(user);
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public void clearAll() {
        userStorage.clearAll();
    }

}
