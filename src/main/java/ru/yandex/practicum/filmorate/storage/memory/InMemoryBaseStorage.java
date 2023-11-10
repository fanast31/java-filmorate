
package ru.yandex.practicum.filmorate.storage.memory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.BaseUnit;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class InMemoryBaseStorage<T extends BaseUnit> implements AbstractStorage<T> {

    private final HashMap<Long, T> storage = new HashMap<>();

    protected static final Logger log = LoggerFactory.getLogger(InMemoryBaseStorage.class);

    private Long id = 0L;

    @Override
    public T findById(Long id) {
        return storage.get(id);
    }

    @Override
    public T create(T data) {
        data.setId(++id);
        storage.put(data.getId(), data);
        log.debug("add " + data);
        return data;
    }

    @Override
    public T update(T data) throws DataNotFoundException {
        if (!storage.containsKey(data.getId())) {
            throw new DataNotFoundException(String.format("data %s not found", data));
        }
        storage.put(data.getId(), data);
        log.debug("update " + data);
        return data;
    }

    @Override
    public List<T> getAll() {
        log.debug("getAll " + storage);
        return new ArrayList<>(storage.values());
    }

    @Override
    public void clearAll() {
        storage.clear();
   }

}
