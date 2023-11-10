package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.BaseUnit;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public abstract class AbstractService<T extends BaseUnit> {

     abstract T create(T data);

     abstract T update(T data) throws DataNotFoundException;

     abstract void clearAll();

     abstract List<T> getAll();

     abstract T findById(Long id) throws DataNotFoundException ;

}
