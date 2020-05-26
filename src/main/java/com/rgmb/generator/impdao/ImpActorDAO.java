package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.ActorDAO;
import com.rgmb.generator.entity.Actor;
import com.rgmb.generator.mappers.ActorRowMapper;
import com.rgmb.generator.mappers.ActorRowMapperForFindId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Класс реализующий все методы интерфейса ActorDAO
 * Аннотация Repository показывает, что данный класс является репозиторием(реализует методы обращения к базе данных)
 * Создает бин с именем actorDAO
 */
@Repository("actorDAO")
/**
 * Все методы класса выполняются в транзакции с уровнем изоляции READ_COMMITTED и propagation = Propagation.REQUIRED
 * (Если у метода нет другой аннотации Transactional)
 */
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
public class ImpActorDAO implements ActorDAO {
    /**
     * Spring JDBC класс для реализации запросов
     */
    @Autowired
    JdbcTemplate template;

    /**
     * Метод поиска id актера по фио
     * @param actorName фио актера
     * @return id актера
     */
    @Override
    public int findIdByActorName(String actorName) {
        String SQL = "SELECT actor_id FROM actors WHERE LOWER(actor_name) = LOWER(?)";
        try {
            return template.queryForObject(SQL, new ActorRowMapperForFindId(), actorName);
        }catch (EmptyResultDataAccessException ex){
            return 0;
        }
    }

    /**
     * Метод добавления актера в базу данных. После добавления возвращается полученный id
     * @param actor {@link com.rgmb.generator.entity.Actor}
     * @return id актера
     */
    @Override
    public int addWithReturningId(Actor actor) {
        String SQL = "INSERT INTO actors(actor_name) VALUES (?) RETURNING actor_id";
        return template.queryForObject(SQL, new ActorRowMapperForFindId(), actor.getName());
    }

    /**
     * Поиск актера по id
     * @param id id актера
     * @return актера или null,если сущность не найдена в базе даннных
     */
    @Override
    public Actor findById(int id) {
        String SQL = "SELECT * FROM actors WHERE actor_id = ?";
        try {
            return template.queryForObject(SQL, new ActorRowMapper(), id);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Добавление актера в базу данных
     * @param actor актер {@link com.rgmb.generator.entity.Actor}
     * @return количество добавленных строк
     */
    @Override
    public int add(Actor actor) {
        String SQL = "INSERT INTO actors(actor_name) VALUES (?)";
        return template.update(SQL, actor.getName());
    }

    /**
     * Запрос всех актеров из базы данных
     * @return массив актеров {@link com.rgmb.generator.entity.Actor}
     */
    @Override
    public List<Actor> findAll() {
        String SQL = "SELECT * FROM actors";
        try {
            return template.query(SQL, new ActorRowMapper());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Обновление актера по id
     * @param id id актера
     * @param actor актер {@link com.rgmb.generator.entity.Actor}
     * @return количество обновленных строк
     */
    @Override
    public int updateById(int id, Actor actor) {
        String SQL = "UPDATE actors SET actor_name = ? WHERE actor_id = ?";
        return template.update(SQL,actor.getName(),id);
    }

    /**
     * Удаление актера по id
     * @param id id актера
     * @return количество удаленных строк
     */
    @Override
    public int deleteById(int id) {
        String SQL = "DELETE FROM actors WHERE actor_id = ?";
        return template.update(SQL, id);
    }


}
