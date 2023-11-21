package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.BaseUnit;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;
import ru.yandex.practicum.filmorate.storage.UniquePairsSetStorage;

import java.util.List;

@Service
public abstract class AbstractService<T extends BaseUnit> {

     protected final AbstractStorage<T> storage;
     protected final UniquePairsSetStorage uniquePairsSetStorage;

     public AbstractService(AbstractStorage<T> storage, UniquePairsSetStorage uniquePairsSetStorage) {
          this.storage = storage;
          this.uniquePairsSetStorage = uniquePairsSetStorage;
     }

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
