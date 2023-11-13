package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.memory.InMemoryUserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService extends AbstractService<User> {

    private final InMemoryUserStorage userStorage;

    public void addFriend(Long id1, Long id2) throws DataNotFoundException {
        User user1 = findById(id1);
        User user2 = findById(id2);
        user1.getFriendsId().add(id2);
        user2.getFriendsId().add(id1);
    }

    public void removeFriend(Long id1, Long id2) throws DataNotFoundException {
        User user1 = findById(id1);
        User user2 = findById(id2);
        user1.getFriendsId().remove(id2);
        user2.getFriendsId().remove(id1);
    }

    private List<User> getUsersById(Set<Long> usersId) throws DataNotFoundException {
        return usersId.stream()
                .map(this::findById)
                .collect(Collectors.toList());
    }

    public List<User> getFriends(Long userId) throws DataNotFoundException {
        User user1 = findById(userId);
        return getUsersById(user1.getFriendsId());
    }

    public List<User> getCommonFriends(Long id1, Long id2) throws DataNotFoundException {

        User user1 = findById(id1);
        User user2 = findById(id2);

        Set<Long> commonFriends = new HashSet<>(user1.getFriendsId());
        commonFriends.retainAll(user2.getFriendsId());

        return getUsersById(commonFriends);

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
