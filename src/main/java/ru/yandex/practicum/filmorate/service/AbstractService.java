package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.BaseUnit;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

import java.util.List;

public abstract class AbstractService<T extends BaseUnit> {

     AbstractStorage<T> storage;

     public T create(T data) {
          return storage.create(data);
     }

     public T update(T data) throws DataNotFoundException {
          return storage.update(data);
     }

     public void clearAll() {
          storage.clearAll();
     }

     public List<T> getAll() {
          return storage.getAll();
     }

     public T findById(Long id) throws DataNotFoundException {
          T unit = storage.findById(id);
          if (unit == null) {
               throw new DataNotFoundException("Unit with id = " + id + " not found");
          }
          return unit;
     }
}
