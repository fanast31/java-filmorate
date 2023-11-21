package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.BaseUnit;

import java.util.List;

public interface AbstractStorage<T extends BaseUnit> {

    T findById(Long id);

    T create(T data);

    T update(T data) throws DataNotFoundException;

    List<T> getAll();

    void clearAll();

}
