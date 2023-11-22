package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film findById(Long id) {
        return findById(id, new HashMap<>());
    }

    public Film findById(Long id, HashMap<Long, MPA> allMpa) {

        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from films where id = ?", id);

        if(userRows.next()) {

            MPA mpa = allMpa.get((long) userRows.getInt("mpa_id"));

            return Film.builder()
                    .id(userRows.getLong("id"))
                    .name(userRows.getString("name"))
                    .description(userRows.getString("description"))
                    .releaseDate(userRows.getDate("releaseDate").toLocalDate())
                    .duration(userRows.getInt("duration"))
                    .mpa(mpa)
                    .build();
        } else {
            throw new DataNotFoundException("Unit with id = " + id + " not found");
        }
    }

    @Override
    public Film create(Film data) {

        String sql = "INSERT INTO films (name, description, releaseDate, duration, mpa_id)" +
                " VALUES (?, ?, ?, ?, ?) RETURNING id";

        Long mpaId = null;
        if (data.getMpa() != null) {
            mpaId = data.getMpa().getId();
        }

        Long generatedId = jdbcTemplate.queryForObject(
                sql,
                Long.class,
                data.getName(),
                data.getDescription(),
                data.getReleaseDate(),
                data.getDuration(),
                mpaId
        );

        data.setId(generatedId);

        return data;
    }

    @Override
    public Film update(Film data) throws DataNotFoundException {

        String sql = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, mpa_id = ? " +
                "WHERE id = ?";

        Long mpaId = null;
        if (data.getMpa() != null) {
            mpaId = data.getMpa().getId();
        }

        int rowsAffected = jdbcTemplate.update(
                sql,
                data.getName(),
                data.getDescription(),
                data.getReleaseDate(),
                data.getDuration(),
                mpaId,
                data.getId()
        );

        if (rowsAffected == 0) {
            throw new DataNotFoundException("No film found with ID: " + data.getId());
        }

        return data;
    }

    @Override
    public List<Film> getAll() {
        throw new UnsupportedOperationException("the command is not supported");
    }

    public List<Film> getAll(HashMap<Long, MPA> allMpa) {
        String sql = "SELECT id, name, description, releaseDate, duration, mpa_id FROM films";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            MPA mpa = allMpa.get(rs.getLong("mpa_id"));

            return Film.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .description(rs.getString("description"))
                    .releaseDate(rs.getDate("releaseDate").toLocalDate())
                    .duration(rs.getInt("duration"))
                    .mpa(mpa)
                    .build();
        });
    }

    @Override
    public void clearAll() {
        throw new UnsupportedOperationException("the command is not supported");
    }
}
