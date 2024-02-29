package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.mpa.MpaDao;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaDao mpaDao;

    public List<Mpa> getAll() {

        return mpaDao.getAll();
    }

    public Mpa getMpaById(Long id) {
        Mpa mpa = mpaDao.getMpaById(id);
        if (mpa == null) {
            throw new NotFoundException("This genre does not exist");
        }
        return mpa;
    }
}
