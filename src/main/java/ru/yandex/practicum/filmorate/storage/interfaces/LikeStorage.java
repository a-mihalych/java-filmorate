package ru.yandex.practicum.filmorate.storage.interfaces;

public interface LikeStorage {

    void addLike(int id, int userId);

    void deleteLike(int id, int userId);
}
