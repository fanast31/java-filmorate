package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.KeyNotGeneratedException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import org.springframework.jdbc.support.KeyHolder;
import ru.yandex.practicum.filmorate.exception.InvalidMPAValue;

import java.sql.PreparedStatement;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film findById(Long id) {

        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from films where id = ?", id);

        if (userRows.next()) {

            return Film.builder()
                    .id(userRows.getLong("id"))
                    .name(userRows.getString("name"))
                    .description(userRows.getString("description"))
                    .releaseDate(userRows.getDate("releaseDate").toLocalDate())
                    .duration(userRows.getInt("duration"))
                    .mpaId((long) userRows.getInt("mpa_id"))
                    .build();
        } else {
            return null;
        }
    }

    @Override
    public Film create(Film data) {

        String sql = "INSERT INTO films (name, description, releaseDate, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";

        if (data.getMpa() == null) {
            throw new InvalidMPAValue("Invalid MPA Value");
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                    ps.setString(1, data.getName());
                    ps.setString(2, data.getDescription());
                    ps.setObject(3, data.getReleaseDate()); // Предполагается, что releaseDate - это объект, например, java.sql.Date
                    ps.setInt(4, data.getDuration());
                    ps.setLong(5, data.getMpa().getId());
                    return ps;
                },
                keyHolder
        );

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new KeyNotGeneratedException("Key was not generated");
        }
        data.setId(key.longValue());

        return data;
    }

    @Override
    public Film update(Film data) {

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
        String sql = "SELECT id, name, description, releaseDate, duration, mpa_id FROM films";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return Film.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .description(rs.getString("description"))
                    .releaseDate(rs.getDate("releaseDate").toLocalDate())
                    .duration(rs.getInt("duration"))
                    .mpaId((long) rs.getInt("mpa_id"))
                    .build();
        });
    }

    @Override
    public void clearAll() {
        throw new UnsupportedOperationException("the command is not supported");
    }
}
