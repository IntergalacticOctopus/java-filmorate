package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
        return mpaDao.getMpaById(id);
    }
}
