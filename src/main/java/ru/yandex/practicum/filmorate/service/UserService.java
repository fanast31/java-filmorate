package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService extends AbstractService<User> {

    public UserService(AbstractStorage<User> storage) {
        super(storage);
    }

    public void addFriend(Long id, Long friendId) throws DataNotFoundException {
        User user = findById(id);
        findById(friendId);
        user.getFriendsId().add(friendId);
    }

    public void removeFriend(Long id, Long friendId) throws DataNotFoundException {
        User user = findById(id);
        findById(friendId);
        user.getFriendsId().remove(friendId);
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
