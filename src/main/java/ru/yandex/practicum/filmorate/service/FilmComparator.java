package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;

public class FilmComparator implements Comparator<Film> {
    @Override
    public int compare(Film film1, Film film2) {
        return Long.compare(film1.getLikesCounter(), film2.getLikesCounter());
    }
}
