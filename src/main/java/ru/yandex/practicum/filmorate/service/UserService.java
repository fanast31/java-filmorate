package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;
import ru.yandex.practicum.filmorate.storage.db.UniquePairsSetDbStorage.FriendsDbStorage;

import java.util.List;

@Service
public class UserService extends AbstractService<User> {
    private final FriendsDbStorage friendsDbStorage;

    public UserService(@Qualifier("userDbStorage") AbstractStorage<User> storage, FriendsDbStorage friendsDbStorage) {
        super(storage);
        this.friendsDbStorage = friendsDbStorage;
    }

    public void addFriend(Long id, Long friendId) {
        findById(id);
        findById(friendId);
        friendsDbStorage.mergePair(id, friendId);
    }

    public void removeFriend(Long id, Long friendId) {
        findById(id);
        findById(friendId);
        friendsDbStorage.removePair(id, friendId);
    }

    public List<User> getFriends(Long userId) {
        return friendsDbStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Long id1, Long id2) {
        findById(id1);
        findById(id2);
        return friendsDbStorage.getCommonFriends(id1, id2);
    }


    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        return storage.create(user);
    }

    @Override
    public User update(User user) {
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        return storage.update(user);
    }
}
