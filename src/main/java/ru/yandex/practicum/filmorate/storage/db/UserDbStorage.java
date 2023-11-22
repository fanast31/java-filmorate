package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User findById(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", id);
        if(userRows.next()) {
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

        String sql = "INSERT INTO users (email, login, name, birthday)" +
                " VALUES (?, ?, ?, ?) RETURNING id";

        Long generatedId = jdbcTemplate.queryForObject(
                sql,
                Long.class,
                data.getEmail(),
                data.getLogin(),
                data.getName(),
                data.getBirthday()
        );

        data.setId(generatedId);

        return data;
    }

    @Override
    public User update(User data) throws DataNotFoundException {

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
        return null;
    }

    @Override
    public void clearAll() {

    }
}
