package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

@Component
public class InMemoryMPAStorage extends InMemoryBaseStorage<MPA> implements MPAStorage {
    public InMemoryMPAStorage() {

        create(MPA.builder()
                .id(1L)
                .name("G")
                .build());
        create(MPA.builder()
                .id(2L)
                .name("PG")
                .build());
        create(MPA.builder()
                .id(3L)
                .name("PG-13")
                .build());
        create(MPA.builder()
                .id(4L)
                .name("R")
                .build());
        create(MPA.builder()
                .id(5L)
                .name("NC-17")
                .build());

    }
}
