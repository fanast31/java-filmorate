package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;
import ru.yandex.practicum.filmorate.storage.db.UniquePairsSetDbStorage.FriendsDbStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService extends AbstractService<User> {
    private final FriendsDbStorage uniquePairsSetStorage;

    public UserService(AbstractStorage<User> storage, FriendsDbStorage uniquePairsSetStorage) {
        super(storage);
        this.uniquePairsSetStorage = uniquePairsSetStorage;
    }

    public void addFriend(Long id, Long friendId) throws DataNotFoundException {
        findById(id);
        findById(friendId);
        uniquePairsSetStorage.mergePair(id, friendId);
    }

    public void removeFriend(Long id, Long friendId) throws DataNotFoundException {
        findById(id);
        findById(friendId);
        uniquePairsSetStorage.removePair(id, friendId);
    }

    private List<User> getUsersById(Set<Long> usersId) throws DataNotFoundException {
        return usersId.stream()
                .map(this::findById)
                .collect(Collectors.toList());
    }

    public List<User> getFriends(Long userId) throws DataNotFoundException {
        findById(userId);
        return getUsersById(uniquePairsSetStorage.getAllKeys2(userId));
    }

    public List<User> getCommonFriends(Long id1, Long id2) throws DataNotFoundException {

        findById(id1);
        findById(id2);

        Set<Long> commonFriends = uniquePairsSetStorage.getAllKeys2(id1);
        commonFriends.retainAll(uniquePairsSetStorage.getAllKeys2(id2));

        return getUsersById(commonFriends);

    }

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        return storage.create(user);
    }

    @Override
    public User update(User user) throws DataNotFoundException {
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        return storage.update(user);
    }
}
