package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

@Component
public class InMemoryMPAStorage extends InMemoryBaseStorage<MPA> implements MPAStorage {
}
