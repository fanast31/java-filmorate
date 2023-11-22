package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.KeyNotGeneratedException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User findById(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", id);
        if (userRows.next()) {
            return User.builder()
                    .id(userRows.getLong("id"))
                    .email(userRows.getString("email"))
                    .login(userRows.getString("login"))
                    .name(userRows.getString("name"))
                    .birthday(userRows.getDate("birthday").toLocalDate())
                    .build();
        } else {
            return null;
        }
    }

    @Override
    public User create(User data) {

        String sql = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                    ps.setString(1, data.getEmail());
                    ps.setString(2, data.getLogin());
                    ps.setString(3, data.getName());
                    ps.setObject(4, data.getBirthday());
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
    public User update(User data) {

        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";

        int rowsAffected = jdbcTemplate.update(
                sql,
                data.getEmail(),
                data.getLogin(),
                data.getName(),
                data.getBirthday(),
                data.getId()
        );

        if (rowsAffected == 0) {
            throw new DataNotFoundException("No film found with ID: " + data.getId());
        }

        return data;
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT id, email, login, name, birthday FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return User.builder()
                    .id(rs.getLong("id"))
                    .email(rs.getString("email"))
                    .login(rs.getString("login"))
                    .name(rs.getString("name"))
                    .birthday(rs.getDate("birthday").toLocalDate())
                    .build();
        });
    }

    @Override
    public void clearAll() {
        throw new UnsupportedOperationException("the command is not supported");
    }
}
