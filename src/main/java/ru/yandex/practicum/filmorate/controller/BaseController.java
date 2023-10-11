package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.IternalServerException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.BaseUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class BaseController<T extends BaseUnit> {
    private final HashMap<Long, T> storage = new HashMap<>();
    protected static final Logger log = LoggerFactory.getLogger(UserController.class);
    private Long id = 0L;

    public T create(T data) throws ValidationException {
        data.setId(++id);
        storage.put(data.getId(), data);
        log.debug("add " + data);
        return data;
    }

    public T update(T data) throws IternalServerException, ValidationException {
        if (!storage.containsKey(data.getId())) {
            throw new IternalServerException(String.format("data %s not found", data));
        }
        storage.put(data.getId(), data);
        log.debug("update " + data);
        return data;
    }

    public List<T> getAll() {
        log.debug("getAll " + storage);
        return new ArrayList<>(storage.values());
    }

    public void clearAll() {
        storage.clear();
    }
}
