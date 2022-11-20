package ru.yandex.practicum.filmorate.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.FilmorateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.*;

@Slf4j
@Service
@NoArgsConstructor
public class UserService {

    UserStorage userStorage;
    ValidationService validationService;

    @Autowired
    public UserService(InMemoryUserStorage userStorage, ValidationService validationService) {
        this.userStorage = userStorage;
        this.validationService = validationService;
    }

    public User createUser(User user) throws ValidationException, FilmorateException {
        user = validationService.validationUser("Создание ", user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) throws ValidationException, FilmorateException {
        user = validationService.validationUser("Обновление ", user);
        return userStorage.saveUser(user);
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUser(int id) throws NotFoundException {
        id = validationService.validationPositive(id, "id");
        User user = userStorage.getUser(id);
        if (user == null) {
            log.warn("!! Не найден пользователь id={}", id);
            throw new NotFoundException("Не найден пользователь");
        }
        return user;
    }

    public Collection<User> getFriendsForIdUser(int id) throws NotFoundException {
        id = validationService.validationPositive(id, "id");
        Collection<Integer> idFriends = userStorage.getIdFriendsForIdUser(id);
        if (idFriends == null) {
            log.warn("!! Не найден пользователь id={}", id);
            throw new NotFoundException("");
        }
        List<User> friends = new ArrayList<>();
        for (Integer idFriend : idFriends) {
            friends.add(userStorage.getUser(idFriend));
        }
        return friends;
    }

    public Collection<User> mutualFriends(int id, int otherId) throws NotFoundException {
        id = validationService.validationPositive(id, "id");
        otherId = validationService.validationPositive(otherId, "otherId");
        Set<User> friendsMutual = new HashSet<>(getFriendsForIdUser(id));
        friendsMutual.addAll(getFriendsForIdUser(otherId));
        friendsMutual.remove(getUser(id));
        friendsMutual.remove(getUser(otherId));
        return friendsMutual;
    }

    public void addFriend(int id, int friendId) throws NotFoundException {
        id = validationService.validationPositive(id, "id");
        friendId = validationService.validationPositive(friendId, "friendId");
        if ((userStorage.getUser(id) == null) || (userStorage.getUser(friendId) == null)) {
            log.warn("!! Не найден пользователь для добовления в друзья");
            throw new NotFoundException("");
        }
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(int id, int friendId) throws NotFoundException {
        id = validationService.validationPositive(id, "id");
        friendId = validationService.validationPositive(friendId, "friendId");
        if ((userStorage.getUser(id) == null) || (userStorage.getUser(friendId) == null)) {
            log.warn("!!! Не найден пользователь для удаления из друзей");
            throw new NotFoundException("");
        }
        userStorage.deleteFriend(id, friendId);
    }
}
