package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

@Component
public class InMemoryGenreStorage extends InMemoryBaseStorage<Genre> implements GenreStorage {
}
