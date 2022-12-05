package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

    private Map<Integer, User> users = new HashMap<>();
    private Map<Integer, Set<Integer>> friends = new HashMap<>();
    private int id = 1;

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User getUser(int id) {
        return users.get(id);
    }

    @Override
    public User createUser(User user) {
        user.setId(nextId());
        friends.put(user.getId(), new HashSet<>());
        return saveUser(user);
    }

    @Override
    public User saveUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User deleteUser(Integer id) {
        for (Integer friend : friends.get(id)) {
            friends.get(friend).remove(id);
        }
        return users.remove(id);
    }

    @Override
    public void addFriend(int user, int friend) {
        friends.get(user).add(friend);
        friends.get(friend).add(user);
    }

    @Override
    public void deleteFriend(int user, int friend) {
        friends.get(user).remove(friend);
        friends.get(friend).remove(user);
    }

    @Override
    public Collection<Integer> getIdFriendsForIdUser(int id) {
        return friends.get(id);
    }

    private int nextId() {
        return id++;
    }
}
