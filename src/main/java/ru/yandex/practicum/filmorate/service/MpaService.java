package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaStorage mpaStorage;
    private final ValidationService validationService;

    public Collection<Mpa> getMpa() {
        return mpaStorage.getMpa();
    }

    public Mpa getMpaForId(int id) {
        id = validationService.validationPositive(id);
        validationService.validationNotFoundIdMpa(id);
        return mpaStorage.getMpaForId(id);
    }
}
