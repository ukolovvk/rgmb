package com.rgmb.generator.impdao;

import com.rgmb.generator.dao.BookDAO;
import com.rgmb.generator.entity.Author;
import com.rgmb.generator.entity.Book;
import com.rgmb.generator.entity.Genre;
import com.rgmb.generator.mappers.BookRowMapper;
import com.rgmb.generator.mappers.BookRowMapperForFindByBookName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.List;

/**
 * Класс реализующий все методы интерфейса BookDAO
 * Аннотация Repository показывает, что данный класс является репозиторием(реализует методы обращения к базе данных)
 * Создает бин с именем bookDAO
 */
@Repository("bookDAO")
/**
 * Все методы класса выполняются в транзакции с уровнем изоляции READ_COMMITTED
 * (Если у метода нет другой аннотации Transactional)
 */
@Transactional(isolation = Isolation.READ_COMMITTED)
public class ImpBookDAO implements BookDAO {
    /**
     * Spring JDBC класс для реализации запросов
     */
    @Autowired
    private JdbcTemplate template;

    /**
     * Класс, реализующих методы обращения к вспомогательной таблице книг и жанров
     * @see com.rgmb.generator.impdao.ImpBookUnionGenresDAO
     */
    @Autowired
    @Qualifier("BookUnionGenresDAO")
    private ImpBookUnionGenresDAO bookUnionGenresDAO;

    /**
     * Класс, реализующих методы обращения к вспомогательной таблице книг и авторов
     * @see com.rgmb.generator.impdao.ImpBookUnionAuthorsDAO
     */
    @Autowired
    @Qualifier("BookUnionAuthorsDAO")
    private ImpBookUnionAuthorsDAO bookUnionAuthorsDAO;

    /**
     * Класс, реализующий методы обращения к таблице жанров
     * @see com.rgmb.generator.impdao.ImpGenreDAO
     */
    @Autowired
    @Qualifier("genreDAO")
    private ImpGenreDAO genreDAO;

    /**
     * Класс, реализующий методы обращения к таблице авторов
     * @see com.rgmb.generator.impdao.ImpAuthorDAO
     */
    @Autowired
    @Qualifier("authorDAO")
    private ImpAuthorDAO authorDAO;

    /**
     * Основной запрос, объединяющий все таблицы
     */
    private final String generalSql = "WITH books_genres_table AS (SELECT books.book_id, string_agg(genre.genre_name,',') as genres\n" +
            "FROM books LEFT JOIN books_union_genres AS bug\n" +
            "ON books.book_id = bug.book_id\n" +
            "LEFT JOIN genre \n" +
            "ON bug.genre_id = genre.id\n" +
            "GROUP BY (books.book_id)\n" +
            "), books_authors_table AS (SELECT books.book_id, string_agg(authors.name,',') as authors\n" +
            "FROM books LEFT JOIN books_union_authors AS bua\n" +
            "ON books.book_id = bua.book_id\n" +
            "LEFT JOIN authors \n" +
            "ON bua.author_id = authors.id\n" +
            "GROUP BY (books.book_id)\n" +
            ")\n" +
            "SELECT books.book_id, books.title, bgt.genres, bat.authors, books.rating, books.page_count, books.year, books.annotation, books.image \n" +
            "FROM books\n" +
            "LEFT JOIN books_genres_table AS bgt \n" +
            "ON books.book_id = bgt.book_id\n" +
            "LEFT JOIN books_authors_table AS bat\n" +
            "ON books.book_id = bat.book_id ";

    /**
     * Поиск книги по id
     * @param id id книги
     * @return книга или null
     */
    @Override
    public Book findById(int id) {
        String SQL = generalSql + " WHERE books.book_id = ?";
        try {
            return template.queryForObject(SQL, new BookRowMapper(), id);
        }catch(EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Добавление книги, а также добавление соответствуюших сущностей в соответствующие таблицы
     * @param book книга {@link com.rgmb.generator.entity.Book}
     * @return количество добавленных книг (1 или 0)
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,propagation = Propagation.REQUIRED)
    public int add(Book book) {
        if(book.getGenres() == null || book.getAuthors() == null)
            return 0;
        String SQL = "INSERT INTO books(title,page_count,rating,year, annotation, image) VALUES(?,?,?,?,?,?) RETURNING book_id";
        int bookID = template.queryForObject(SQL,new BookRowMapperForFindByBookName(), book.getTitle(),book.getSize(),book.getRating(),book.getYear(),book.getAnnotation(),book.getImageName());
        for(Genre genre : book.getGenres()){
            int genreID = genreDAO.findIdByGenreName(genre.getName());
            if(genreID == 0)
                genreID = genreDAO.addWithReturningId(genre);
            bookUnionGenresDAO.add(bookID,genreID);
        }
        for(Author author : book.getAuthors()){
            int authorID = authorDAO.findIdByAuthorName(author.getName());
            if(authorID == 0)
                authorID = authorDAO.addWithReturningId(author);
            bookUnionAuthorsDAO.add(bookID,authorID);
        }
        return 1;
    }

    /**
     * Запрос всех книг из базы данных
     * @return массив книг {@link com.rgmb.generator.entity.Book} или null, если таблица пуста
     */
    @Override
    public List<Book> findAll() {
        String SQL = generalSql;
        try {
            return template.query(SQL, new BookRowMapper());
        }catch(EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Удаление книги по id
     * @param id id книги
     * @return количество удаленных книг
     */
    @Override
    public int deleteById(int id) {
        String SQL = "DELETE FROM books WHERE book_id = ?";
        return template.update(SQL,id);
    }

    /**
     * Обновление описания по id
     * @param id id книги
     * @param annotation описание книги
     * @return количество обновленных строк
     */
    @Override
    public int updateAnnotationById(int id, String annotation) {
        String SQL = "UPDATE books SET annotation = ? WHERE book_id = ?";
        return template.update(SQL,annotation,id);
    }

    /**
     * Обновление количества страниц книги
     * @param id id книги
     * @param size количество страниц книги
     * @return количество обновленных строк
     */
    @Override
    public int updateSizeById(int id, int size) {
        String SQL = "UPDATE books SET page_count = ? WHERE book_id = ?";
        return template.update(SQL,size,id);
    }

    /**
     * Обновление рейтинга книги по id
     * @param id id книги
     * @param rating рейтинг книги
     * @return количество обновленных строк
     */
    @Override
    public int updateRatingById(int id, double rating) {
        String SQL = "UPDATE books SET rating = ? WHERE book_id = ?";
        return template.update(SQL,rating,id);
    }

    /**
     * Обновление года написания книги по id
     * @param id id книги
     * @param year год написания книги
     * @return количество обновленных строк
     */
    @Override
    public int updateYearById(int id, int year) {
        String SQL = "UPDATE books SET year = ? WHERE book_id = ?";
        return template.update(SQL,year,id);
    }

    /**
     * Обновление названия книги по id
     * @param id id книги
     * @param title название книги
     * @return количество обновленных строк
     */
    @Override
    public int updateTitleById(int id, String title) {
        String SQL = "UPDATE books SET title = ? WHERE book_id = ?";
        return template.update(SQL,title,id);
    }

    /**
     * Обновление авторов книги по id
     * @param id id книги
     * @param authors массив авторов книги {@link com.rgmb.generator.entity.Author}
     * @return количество обновленных строк
     */
    @Override
    public int updateAuthorsById(int id, List<Author> authors) {
        String SQL = "DELETE FROM books_union_authors WHERE book_id = ?";
        template.update(SQL,id);
        for(Author author : authors){
            int authorID = authorDAO.findIdByAuthorName(author.getName());
            if(authorID == 0)
                authorID = authorDAO.addWithReturningId(author);
            bookUnionAuthorsDAO.add(id,authorID);
        }
        return authors.size();
    }

    /**
     * Обновление жанров книги по id
     * @param id id книги
     * @param genres массив жанров книги {@link com.rgmb.generator.entity.Genre}
     * @return количество обновленных строк
     */
    @Override
    public int updateGenreById(int id, List<Genre> genres) {
        String SQL = "DELETE FROM books_union_genres WHERE book_id = ?";
        template.update(SQL,id);
        for(Genre genre : genres){
            int genreID = genreDAO.findIdByGenreName(genre.getName());
            if(genreID == 0)
                genreID = genreDAO.addWithReturningId(genre);
            bookUnionGenresDAO.add(id,genreID);
        }
        return genres.size();
    }

    /**
     * Обновление постера книги по id
     * @param id id книги
     * @param imageName url постера
     * @return количество обновленных строк
     */
    @Override
    public int updateImageNameById(int id, String imageName) {
        String SQL = "UPDATE books SET image = ? WHERE book_id = ?";
        return template.update(SQL,imageName,id);
    }

    /**
     * Получение минимального рейтинга среди книг из базы данных.
     * Метод нужен для генерации по фильтрам в контроллере
     * @return минимальное значение рейтинга
     */
    @Override
    public double getMinRating() {
        String SQL = "SELECT MIN(rating) AS min_rating FROM books";
        return template.queryForObject(SQL, (ResultSet resultSet, int i) -> resultSet.getDouble("min_rating"));
    }

    /**
     * Получение максимального рейтинга среди книг из базы данных.
     * Метод нужен для генерации по фильтрам в контроллере
     * @return максимальное значение рейтинга
     */
    @Override
    public double getMaxRating() {
        String SQL = "SELECT MAX(rating) AS max_rating FROM books";
        return template.queryForObject(SQL, (ResultSet resultSet, int i) -> resultSet.getDouble("max_rating"));
    }

    /**
     * Поиск книг по названию
     * Возвращается массив книг в том случае, если в базе будет несколько книг
     * с одинаковым названием.
     * @param title название книги
     * @return массив книг
     */
    @Override
    public List<Book> findByTitle(String title) {
        String SQL = generalSql + " WHERE LOWER(title) = LOWER(?)";
        try {
            return template.query(SQL, new BookRowMapper(), title);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Получение рандомной книги из базы данных
     * @return книга или null
     */
    @Override
    public Book getRandomBook() {
        String SQL = generalSql + " ORDER BY RANDOM() LIMIT 1";
        try {
            return template.queryForObject(SQL, new BookRowMapper());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Получение рандомной книги с указанным жанром из базы данных
     * @param genre жанр книги {@link com.rgmb.generator.entity.Genre}
     * @return книга или null
     */
    @Override
    public Book getRandomBook(Genre genre) {
        String SQL = generalSql + "WHERE bgt.genres ILIKE '%" + genre.getName() + "%' ORDER BY RANDOM() LIMIT 1";
        try {
            return template.queryForObject(SQL, new BookRowMapper());
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Получения рандомной книги с указанным рейтингом из базы данных
     * @param FirstRating левая граница отрезка
     * @param SecondRating правая граница отрезка
     * @return книга или null
     */
    @Override
    public Book getRandomBook(double FirstRating, double SecondRating) {
        String SQL = generalSql + "WHERE (books.rating BETWEEN ? AND ?)  ORDER BY RANDOM() LIMIT 1";
        try {
            return template.queryForObject(SQL, new BookRowMapper(), FirstRating, SecondRating);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Получение рандомной книги с указанным количеством страниц из базы данных
     * @param minSize левая граница отрезка
     * @param maxSize правая граница отрезка
     * @return книга или null
     */
    @Override
    public Book getRandomBook(int minSize, int maxSize) {
        String SQL = generalSql + "WHERE  (books.page_count BETWEEN ? AND ?)  ORDER BY RANDOM() LIMIT 1";
        try {
            return template.queryForObject(SQL, new BookRowMapper(), minSize, maxSize);
        }catch(EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Получение рандомной книги с указанными жанром и рейтингом из базы данных
     * @param genre жанр книги {@link com.rgmb.generator.entity.Genre}
     * @param FirstRating левая граница отрезка
     * @param SecondRating правая граница отрезка
     * @return книга или null
     */
    @Override
    public Book getRandomBook(Genre genre, double FirstRating, double SecondRating) {
        String SQL = generalSql + "WHERE (books.rating BETWEEN ? AND ?) AND bgt.genres ILIKE '%" + genre.getName() + "%'  ORDER BY RANDOM() LIMIT 1";
        try {
            return template.queryForObject(SQL, new BookRowMapper(), FirstRating, SecondRating);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Получение рандомной книги с указанными жанром и количеством страниц
     * @param genre жанр книги {@link com.rgmb.generator.entity.Genre}
     * @param minSize левая граница отрезка
     * @param maxSize правая граница отрезка
     * @return книга или null
     */
    @Override
    public Book getRandomBook(Genre genre, int minSize, int maxSize) {
        String SQL = generalSql + "WHERE (books.page_count BETWEEN ? AND ?) AND bgt.genres ILIKE '%" + genre.getName() + "%'  ORDER BY RANDOM() LIMIT 1";
        try {
            return template.queryForObject(SQL, new BookRowMapper(), minSize, maxSize);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Получение рандомной книги с указанными рейтингом и количеством страниц
     * @param FirstRating левая граница отрезка
     * @param SecondRating правая граница отрезка
     * @param minSize левая граница отрезка
     * @param maxSize правая граница отрезка
     * @return книга или null
     */
    @Override
    public Book getRandomBook(double FirstRating, double SecondRating, int minSize, int maxSize) {
        String SQL = generalSql + "WHERE (books.page_count BETWEEN ? AND ?) AND  (books.rating BETWEEN ? AND ?) ORDER BY RANDOM() LIMIT 1";
        try {
            return template.queryForObject(SQL, new BookRowMapper(), minSize, maxSize, FirstRating, SecondRating);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    /**
     * Получение рандомной книги с указанными жанром, рейтингом и количеством страниц
     * @param genre жанр книги {@link com.rgmb.generator.entity.Genre}
     * @param FirstRating левая граница отрезка
     * @param SecondRating правая граница отрезка
     * @param minSize левая граница отрезка
     * @param maxSize правая граница отрезка
     * @return книги или null
     */
    @Override
    public Book getRandomBook(Genre genre, double FirstRating, double SecondRating, int minSize, int maxSize) {
        String SQL = generalSql + "WHERE (books.page_count BETWEEN ? AND ?) AND  (books.rating BETWEEN ? AND ?) AND (bgt.genres ILIKE '%" + genre.getName() + "%') ORDER BY RANDOM() LIMIT 1";
        try {
            return template.queryForObject(SQL, new BookRowMapper(), minSize, maxSize, FirstRating, SecondRating);
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

}
