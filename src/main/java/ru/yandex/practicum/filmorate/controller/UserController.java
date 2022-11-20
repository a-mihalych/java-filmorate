package ru.yandex.practicum.filmorate.controller;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.FilmorateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
@NoArgsConstructor
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) throws ValidationException, FilmorateException {
        log.info("! Запрос на добавление нового пользователя {}.", user);
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException, FilmorateException {
        log.info("! Запрос на обновление пользователя с id={} {}.", user.getId(), user);
        return userService.updateUser(user);
    }

    @GetMapping
    public Collection<User> getUsers() {
        log.info("! Запрос на получение списка пользователей.");
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) throws NotFoundException {
        log.info("! Запрос на получение пользователя по id={}.", id);
        return userService.getUser(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriendsForIdUser(@PathVariable int id) throws NotFoundException {
        log.info("! Запрос на получение списка друзей пользователя с id={}.", id);
        return userService.getFriendsForIdUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> mutualFriends(@PathVariable int id, @PathVariable int otherId) throws NotFoundException {
        log.info("! Запрос на получение списка общих друзей id={} и id={}.", id, otherId);
        return userService.mutualFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) throws NotFoundException {
        log.info("! Запрос на добавления в друзья id={} и id={}.", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) throws NotFoundException {
        log.info("! Запрос на удаление из друзей id={} и id={}.", id, friendId);
        userService.deleteFriend(id, friendId);
    }
}
