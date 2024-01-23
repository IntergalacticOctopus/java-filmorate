package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class Film extends BaseUnit {

    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @Min(1)
    private int duration;
}
