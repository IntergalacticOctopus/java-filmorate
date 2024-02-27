package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FilmControllerTest {

    public static final String PATH = "/films";
    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {

    }

    @Test
    void createNegative() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContentFromFile("controller/request/wrongFilm.json")))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }


    @Test
    void validateFilmDate() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(1500, 1, 1))
                .duration(100L)
                .build();

        ValidateService validateService = new ValidateService();

        assertThrows(ValidationException.class, () -> validateService.validate(film));

    }

    @Test
    void validateFilmName() {
        Film film = Film.builder()
                .name("")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100L)
                .build();

        ValidateService validateService = new ValidateService();

        assertThrows(ValidationException.class, () -> validateService.validate(film));

    }

    @Test
    void validateFilmDescription() {
        Film film = Film.builder()
                .name("Name")
                .description("DescriptionDescriptionDescriptionDescriptionDescr" +
                        "iptionDescriptionDescriptionDescriptionDescriptionDescriptionDesc" +
                        "riptionDescriptionDescriptionDescriptionDescriptionDescriptionDescript" +
                        "ionDescriptionDescriptionDescription")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100L)
                .build();

        ValidateService validateService = new ValidateService();

        assertThrows(ValidationException.class, () -> validateService.validate(film));

    }

    @Test
    void validateFilmDuration() {
        Film film = Film.builder()
                .name("Name")
                .description("DescriptionDescriptionDescriptionDescriptionDescr" +
                        "iptionDescriptionDescriptionDescriptionDescriptionDescriptionDesc" +
                        "riptionDescriptionDescriptionDescriptionDescriptionDescriptionDescript" +
                        "ionDescriptionDescriptionDescription")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(-100L)
                .build();

        ValidateService validateService = new ValidateService();

        assertThrows(ValidationException.class, () -> validateService.validate(film));

    }


    private String getContentFromFile(String filename) {
        try {
            return Files.readString(ResourceUtils.getFile("classpath:" + filename).toPath(),
                    StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }

}