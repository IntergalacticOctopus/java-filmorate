package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.mpa.MpaStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaStorage mpaStorage;

    public List<Mpa> getAll() {

        return mpaStorage.getAll();
    }

    public Mpa getById(Long id) {
        Mpa mpa = mpaStorage.getById(id);
        if (mpa == null) {
            throw new NotFoundException("This genre does not exist");
        }
        return mpa;
    }
}
