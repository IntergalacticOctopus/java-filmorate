package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@SuperBuilder
public class Genre {
    @NotNull
    private Long id;
    @NotNull
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return Objects.equals(id, genre.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);

    }
}