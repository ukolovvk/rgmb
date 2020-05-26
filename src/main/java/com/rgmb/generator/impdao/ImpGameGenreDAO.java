package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.GameGenreDAO;
import com.rgmb.generator.entity.GameGenre;
import com.rgmb.generator.mappers.GameGenreRowMapper;
import com.rgmb.generator.mappers.GameGenreRowMapperForFindById;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Класс реализующий все методы интерфейса GameGenreDAO
 * Аннотация Repository показывает, что данный класс является репозиторием(реализует методы обращения к базе данных)
 * Создает бин с именем GameGenreDAO
 */
@Repository("GameGenreDAO")
/**
 * Все методы класса выполняются в транзакции с уровнем изоляции READ_COMMITTED и propagation = Propagation.REQUIRED
 * (Если у метода нет другой аннотации Transactional)
 */
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
public class ImpGameGenreDAO implements GameGenreDAO {
    /**
     * Spring JDBC класс для реализации запросов
     */
    @Autowired
    JdbcTemplate template;

    /**
     * Метод поиска id жанра игр по названию
     * @param gameGenreName название жанра
     * @return id жанра
     */
    @Override
    public int findIdByGameGenreName(String gameGenreName) {
        String SQL = "SELECT genre_id FROM game_genres WHERE LOWER(genre_name) = LOWER(?)";
        try {
            return template.queryForObject(SQL, new GameGenreRowMapperForFindById(), gameGenreName);
        }catch (EmptyResultDataAccessException ex){
            return 0;
        }
    }

    /**
     * Метод добавления жанра в базу данных. После добавления возвращается полученный id
     * @param genre жанр {@link com.rgmb.generator.entity.GameGenre}
     * @return полученный id жанра
     */
    @Override
    public int addWithReturningId(GameGenre genre) {
        String SQL = "INSERT INTO game_genres(genre_name) VALUES (?) RETURNING genre_id";
        return template.queryForObject(SQL,new GameGenreRowMapperForFindById(),genre.getName());
    }

    /**
     * Поиск жанра по id
     * @param id id жанра
     * @return жанр или null,если сущность не найдена в базе даннных
     */
    @Override
    public GameGenre findById(int id) {
        String SQL = "SELECT * FROM game_genres WHERE genre_id = ?";
        try {
            return template.queryForObject(SQL, new GameGenreRowMapper(), id);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Запрос всех жанров игр из базы данных
     * @return массив жанров {@link com.rgmb.generator.entity.GameGenre}
     */
    @Override
    public List<GameGenre> findAll() {
        String SQL = "SELECT * FROM game_genres";
        try {
            return template.query(SQL, new GameGenreRowMapper());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Добавление жанра игр в базу данных
     * @param genre жанр {@link com.rgmb.generator.entity.GameGenre}
     * @return количество добавленных строк
     */
    @Override
    public int add(GameGenre genre) {
        String SQL = "INSERT INTO game_genres(genre_name) VALUES(?)";
        return template.update(SQL,genre.getName());
    }

    /**
     * Обновление жанра игр по id
     * @param id id жанра
     * @param gameGenre жанр {@link com.rgmb.generator.entity.GameGenre}
     * @return количество обновленных строк
     */
    @Override
    public int updateById(int id, GameGenre gameGenre) {
        String SQL = "UPDATE game_genres SET genre_name = ? WHERE genre_id = ?";
        return template.update(SQL, gameGenre.getName(),id);
    }

    /**
     * Удаление жанра игр по id
     * @param id id жанра
     * @return количество удаленных строк
     */
    @Override
    public int deleteById(int id) {
        String SQL = "DELETE FROM game_genres WHERE genre_id = ?";
        return template.update(SQL,id);
    }
}
