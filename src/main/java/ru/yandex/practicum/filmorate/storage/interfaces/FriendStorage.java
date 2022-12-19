package ru.yandex.practicum.filmorate.storage.interfaces;

public interface FriendStorage {

    void addFriend(int user, int friend);

    void deleteFriend(int user, int friend);
}
