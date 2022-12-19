package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final FriendStorage friendStorage;
    private final ValidationService validationService;

    public User createUser(User user) {
        user = validationService.validationUser(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        user = validationService.validationUser(user);
        validationService.validationPositive(user.getId());
        validationService.validationNotFoundIdUser(user.getId());
        return userStorage.saveUser(user);
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUser(int id) {
        id = validationService.validationPositive(id);
        return userStorage.getUser(id);
    }

    public Collection<User> getFriendsForIdUser(int id) {
        id = validationService.validationPositive(id);
        validationService.validationNotFoundIdUser(id);
        return userStorage.getFriendsForIdUser(id);
    }

    public Collection<User> mutualFriends(int id, int otherId) {
        id = validationService.validationPositive(id);
        otherId = validationService.validationPositive(otherId);
        validationService.validationNotFoundIdUser(id);
        validationService.validationNotFoundIdUser(otherId);
        Set<User> friendsMutual = new HashSet<>(userStorage.getFriendsForIdUser(id));
        friendsMutual.addAll(userStorage.getFriendsForIdUser(otherId));
        friendsMutual.remove(getUser(id));
        friendsMutual.remove(getUser(otherId));
        return friendsMutual;
    }

    public void addFriend(int id, int friendId) {
        id = validationService.validationPositive(id);
        friendId = validationService.validationPositive(friendId);
        validationService.validationNotFoundIdUser(id);
        validationService.validationNotFoundIdUser(friendId);
        friendStorage.addFriend(id, friendId);
    }

    public void deleteFriend(int id, int friendId) {
        id = validationService.validationPositive(id);
        friendId = validationService.validationPositive(friendId);
        validationService.validationNotFoundIdUser(id);
        validationService.validationNotFoundIdUser(friendId);
        friendStorage.deleteFriend(id, friendId);
    }
}
