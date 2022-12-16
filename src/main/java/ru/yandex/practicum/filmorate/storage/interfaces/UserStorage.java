package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    Collection<User> getUsers();

    User getUser(int id);

    User createUser(User user);

    User saveUser(User user);

    Integer deleteUser(Integer id);

    void addFriend(int user, int friend);

    void deleteFriend(int user, int friend);

    Collection<Integer> getIdFriendsForIdUser(int id);
}
