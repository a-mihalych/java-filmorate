package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final ValidationService validationService;

    public User createUser(User user) {
        user = validationService.validationUser(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        user = validationService.validationUser(user);
        validationService.validationNotFoundIdUser(user.getId());
        return userStorage.saveUser(user);
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUser(int id) {
        id = validationService.validationPositive(id);
        User user = userStorage.getUser(id);
        if (user == null) {
            log.warn("!! Не найден пользователь id={}", id);
            throw new NotFoundException("Не найден пользователь");
        }
        return user;
    }

    public Collection<User> getFriendsForIdUser(int id) {
        id = validationService.validationPositive(id);
        validationService.validationNotFoundIdUser(id);
        Collection<Integer> idFriends = userStorage.getIdFriendsForIdUser(id);
        List<User> friends = new ArrayList<>();
        for (Integer idFriend : idFriends) {
            friends.add(userStorage.getUser(idFriend));
        }
        return friends;
    }

    public Collection<User> mutualFriends(int id, int otherId) {
        id = validationService.validationPositive(id);
        otherId = validationService.validationPositive(otherId);
        validationService.validationNotFoundIdUser(id);
        validationService.validationNotFoundIdUser(otherId);
        Set<User> friendsMutual = new HashSet<>(getFriendsForIdUser(id));
        friendsMutual.addAll(getFriendsForIdUser(otherId));
        friendsMutual.remove(getUser(id));
        friendsMutual.remove(getUser(otherId));
        return friendsMutual;
    }

    public void addFriend(int id, int friendId) {
        id = validationService.validationPositive(id);
        friendId = validationService.validationPositive(friendId);
        validationService.validationNotFoundIdUser(id);
        validationService.validationNotFoundIdUser(friendId);
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(int id, int friendId) {
        id = validationService.validationPositive(id);
        friendId = validationService.validationPositive(friendId);
        validationService.validationNotFoundIdUser(id);
        validationService.validationNotFoundIdUser(friendId);
        userStorage.deleteFriend(id, friendId);
    }
}
