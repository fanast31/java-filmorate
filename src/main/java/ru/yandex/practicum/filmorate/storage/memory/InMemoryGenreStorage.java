package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

@Component
public class InMemoryGenreStorage extends InMemoryBaseStorage<Genre> implements GenreStorage {
    public InMemoryGenreStorage() {

        create(Genre.builder()
                .id(1L)
                .name("Боевик")
                .build());
        create(Genre.builder()
                .id(2L)
                .name("Комедия")
                .build());
        create(Genre.builder()
                .id(3L)
                .name("Драма")
                .build());
        create(Genre.builder()
                .id(4L)
                .name("Триллер")
                .build());
        create(Genre.builder()
                .id(5L)
                .name("Мультфильм")
                .build());
        create(Genre.builder()
                .id(6L)
                .name("Документальный")
                .build());

    }
}
