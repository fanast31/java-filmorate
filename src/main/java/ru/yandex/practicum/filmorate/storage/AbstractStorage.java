package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.BaseUnit;

import java.util.List;

public interface AbstractStorage<T extends BaseUnit> {

    public T create(T data);

    public T update(T data) throws DataNotFoundException;

    public List<T> getAll();

    public void clearAll();

}
