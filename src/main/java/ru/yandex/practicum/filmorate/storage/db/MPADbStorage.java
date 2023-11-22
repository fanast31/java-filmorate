package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("mPADbStorage")
@RequiredArgsConstructor
public class MPADbStorage implements MPAStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public MPA findById(Long id) {
        String sqlQuery = "select * from MPA where id = ?";
        List<MPA> mpa = jdbcTemplate.query(sqlQuery, MPADbStorage::createMPA, id);
        if (mpa.size() == 0) {
            return null;
        }
        return mpa.get(0);
    }

    @Override
    public List<MPA> getAll() {
        String sqlQuery = "select * from MPA";
        return jdbcTemplate.query(sqlQuery, MPADbStorage::createMPA);
    }

    static MPA createMPA(ResultSet rs, int rowNum) throws SQLException {
        return MPA.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build();
    }

    @Override
    public MPA create(MPA data) {
        throw new UnsupportedOperationException("the command is not supported");
    }

    @Override
    public MPA update(MPA data) {
        throw new UnsupportedOperationException("the command is not supported");
   }

    @Override
    public void clearAll() {
        throw new UnsupportedOperationException("the command is not supported");
    }
}
