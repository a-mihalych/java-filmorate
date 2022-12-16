package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;
    private final ValidationService validationService;

    public Collection<Genre> getGenre() {
        return genreStorage.getGenre();
    }

    public Genre getGenreForId(int id) {
        id = validationService.validationPositive(id);
        validationService.validationNotFoundIdGenre(id);
        return genreStorage.getGenreForId(id);
    }
}
