package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = {"id"})
@SuperBuilder
@NoArgsConstructor(force = true)
public class User {
    private Long id;
    @NotEmpty
    @Email
    private String email;
    @NotBlank
    private String login;

    private String name;
    @PastOrPresent
    private LocalDate birthday;

    private Set<Long>  friends = new LinkedHashSet<>();
    private Set<Long> likes = new LinkedHashSet<>();
}
