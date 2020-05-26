package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.MovieUnionCountriesDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Класс реализующий все методы интерфейса MovieUnionCountriesDAO
 * Аннотация Repository показывает, что данный класс является репозиторием(реализует методы обращения к базе данных)
 * Создает бин с именем MovieUnionCountriesDAO
 */
@Repository("MovieUnionCountriesDAO")
/**
 * Все методы класса выполняются в транзакции с уровнем изоляции READ_COMMITTED и propagation = Propagation.REQUIRED
 */
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
public class ImpMovieUnionCountriesDAO implements MovieUnionCountriesDAO {
    /**
     * Spring JDBC класс для реализации запросов
     */
    @Autowired
    JdbcTemplate template;

    /**
     * Удаление записи из вспомогательной таблицы, связывающей фильмы и страны
     * @param movieID id фильма
     * @return количество удаленных записей
     */
    @Override
    public int delete(int movieID) {
        String SQL = "DELETE FROM movie_union_countries WHERE movie_id = ?";
        return template.update(SQL, movieID);
    }

    /**
     * Добавление записи во вспомогательную таблицу, связывающую фильмы и страны
     * @param movieID id фильма
     * @param countryID id страны
     * @return количество добавленных записей
     */
    @Override
    public int add(int movieID, int countryID) {
        String SQL = "INSERT INTO movie_union_countries(movie_id, country_id) VALUES (?,?)";
        return template.update(SQL,movieID,countryID);
    }
}
