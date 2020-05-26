package com.rgmb.generator.controllers;

import com.rgmb.generator.entity.*;
import com.rgmb.generator.impdao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс контроллер REST для нашего Spring приложения.
 * Реализованы как просто методы получения рандомных фильмов/книг/игр,
 * так и методы их получения в зависимости от заданных параметров.
 * Также присутствуют методы добавления фильмов/книг/игр
 */

@RestController
public class MainController {
    /**
     * Доступ к базе данных книг
     * @see com.rgmb.generator.impdao.ImpBookDAO
     */
    @Autowired
    @Qualifier("bookDAO")
    ImpBookDAO bookDao;

    /**
     * Доступ к базе данных фильмов
     * @see com.rgmb.generator.impdao.ImpMovieDAO
     */
    @Autowired
    @Qualifier("MovieDAO")
    ImpMovieDAO movieDAO;

    /**
     * Доступ к базе данных игр
     * @see com.rgmb.generator.impdao.ImpGameDAO
     */
    @Autowired
    @Qualifier("ImpGameDAO")
    ImpGameDAO gameDAO;

    /**
     * Доступ к базе данных жанров фильмов
     * @see com.rgmb.generator.impdao.ImpMovieGenreDAO
     */
    @Autowired
    @Qualifier("movieGenreDAO")
    ImpMovieGenreDAO movieGenreDAO;

    /**
     * Доступ к базе данных жанров книг
     * @see com.rgmb.generator.impdao.ImpGenreDAO
     */
    @Autowired
    @Qualifier("genreDAO")
    ImpGenreDAO bookGenreDAO;

    /**
     * Доступ к базе данных жанров игр
     * @see com.rgmb.generator.impdao.ImpGameGenreDAO
     */
    @Autowired
    @Qualifier("GameGenreDAO")
    ImpGameGenreDAO gameGenreDAO;

    /**
     * Get метод получения рандомной книги из базы данных по мэппингу /randombook
     * @return ResponseEntity<Book>
     */
    @GetMapping("/randombook")
    public ResponseEntity<Book> getRandomBook(){
        Book book = bookDao.getRandomBook();
        return book == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(book,HttpStatus.OK);
    }

    /**
     * Get метод получения рандомного фильма из базы данных по мэппингу /randommovie
     * @return ResponseEntity<Movie>
     */
    @GetMapping("/randommovie")
    public ResponseEntity<Movie> getRandomMovie(){
        Movie movie = movieDAO.getRandomMovie();
        return movie == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(movie,HttpStatus.OK);
    }

    /**
     * Get метод получения рандомной игры из базы данных по мэппингу /randomgame
     * @return ResponseEntity<Movie>
     */
    @GetMapping("/randomgame")
    public ResponseEntity<Game> getRandomGame(){
        Game game = gameDAO.getRandomGame();
        return game == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(game,HttpStatus.OK);
    }

    /**
     * Post метод добавления игры в базу данных по мэппингу /addgame
     * @param game {@link com.rgmb.generator.entity.Game}
     * @return ResponseEntity<>(HttpStatus.CREATED) or ResponseEntity<>(HttpStatus.BAD_REQUEST)
     */
    @PostMapping("/addgame")
    public ResponseEntity<?> addNewGame(@RequestBody Game game){
        int local = gameDAO.add(game);
        if(local == 1)
            return new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Post метод добавления фильма в базу данных по мэппингу /addmovie
     * @param movie {@link com.rgmb.generator.entity.Movie}
     * @return ResponseEntity<>(HttpStatus.CREATED) or ResponseEntity<>(HttpStatus.BAD_REQUEST)
     */
    @PostMapping("/addmovie")
    public ResponseEntity<?> addNewMovie(@RequestBody Movie movie){
        int local = movieDAO.add(movie);
        if(local == 1)
            return new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Post метод добавления книги в базу данных по мэппингу /addbook
     * @param book {@link com.rgmb.generator.entity.Book}
     * @return ResponseEntity<>(HttpStatus.CREATED) or ResponseEntity<>(HttpStatus.BAD_REQUEST)
     */
    @PostMapping("/addbook")
    public ResponseEntity<?> addNewBook(@RequestBody Book book){
        int local = bookDao.add(book);
        if(local == 1)
            return new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Post метод добавления массива фильмов в базу данных по мэппингу /addmanymovies
     * @param list массив фильмов
     * @return ResponseEntity<>(HttpStatus.OK)
     */
    @PostMapping("/addmanymovies")
    public ResponseEntity<?> addManyMovies(@RequestBody List<Movie> list){
        list.forEach(movie -> movieDAO.add(movie));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Post метод добавления массива книг в базу данных по мэппингу /addmanybooks
     * @param list массив книг
     * @return ResponseEntity<>(HttpStatus.OK)
     */
    @PostMapping("/addmanybooks")
    public ResponseEntity<?> addManyBooks(@RequestBody List<Book> list){
        list.forEach(book -> bookDao.add(book));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Post метод добавления массива игр в базу данных по мэппингу /addmanygames
     * @param games массив игр
     * @return ResponseEntity<>(HttpStatus.OK)
     */
    @PostMapping("/addmanygames")
    public ResponseEntity<?> addManyGames(@RequestBody List<Game> games){
        games.forEach(game -> gameDAO.add(game));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Get метод получения рандомного фильма указанного жанра из базы данных по мэппингу /randommoviewithgenre/movieGenre
     * Если фильма с таки жанром нет - вернется HttpStatus.NOT_FOUND
     * @param movieGenre жанр фильма {@link com.rgmb.generator.entity.MovieGenre}
     * @return ResponseEntity<>(movie,HttpStatus.OK) or ResponseEntity<>(HttpStatus.NOT_FOUND)
     */
    @GetMapping("/randommoviewithgenre/{movieGenre}")
    public ResponseEntity<Movie> getRandomMovieWithGenre(@PathVariable(name = "movieGenre") String movieGenre){
        Movie movie = movieDAO.getRandomMovie(new MovieGenre(movieGenre));
        return movie == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(movie,HttpStatus.OK);
    }

    /**
     * Get метод получения всех жанров фильмов из базы данных по мэппингу /getallmoviegenres
     * @return ResponseEntity<>(localList,HttpStatus.OK) or ResponseEntity<>(HttpStatus.NOT_FOUND)
     */
    @GetMapping("/getallmoviegenres")
    public ResponseEntity<List<MovieGenre>> getAllMovieGenres(){
        List<MovieGenre> localList = movieGenreDAO.findAll();
        List<MovieGenre> resultList = new ArrayList<>();
        for(MovieGenre genre : localList){
            if(!genre.getName().contains(","))
                resultList.add(genre);
        }
        return localList == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(localList,HttpStatus.OK);
    }

    /**
     * Get метод получения рандомного фильма, выпущенного в указанныее годы, из базы данных по мэппингу /randommovieswithyears/yearFirst-yearSecond
     * @param firstYear левая граница отрезка
     * @param secondYear правая граница отрезка
     * @return ResponseEntity<>(movie,HttpStatus.OK) or new ResponseEntity<>(HttpStatus.NOT_FOUND)
     */
    @GetMapping("/randommovieswithyears/{yearFirst}-{yearSecond}")
    public ResponseEntity<Movie> getRandomMovieWithYears(@PathVariable(name = "yearFirst") Integer firstYear,@PathVariable(name = "yearSecond") Integer secondYear){
        Movie movie = movieDAO.getRandomMovie(firstYear,secondYear);
        return movie == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(movie,HttpStatus.OK);
    }

    /**
     * Get метод получения минимального и максимального годов выпуска фильмов по мэппингу /getminmaxyearsmovies
     * @return ResponseEntity<>(localList, HttpStatus.OK)
     */
    @GetMapping("/getminmaxyearsmovies")
    public ResponseEntity<List<Integer>> getMinMaxYearsMovies(){
        int minYear = movieDAO.getMinYear();
        int maxYear = movieDAO.getMaxYear();
        List<Integer> localList = new ArrayList<>(2);
        localList.add(minYear); localList.add(maxYear);
        return new ResponseEntity<>(localList, HttpStatus.OK);
    }

    /**
     * Get метод получения рандомного фильма с заданными жанром и годами выпуска по мэппингу /randommoviewithgenreandyears/genreName/yearFirst_yearSecond
     * @param genreName жанр фильма {@link com.rgmb.generator.entity.MovieGenre}
     * @param firstYear левая граница отрезка
     * @param secondYear праваая граница отрезка
     * @return new ResponseEntity<>(movie,HttpStatus.OK) or ResponseEntity<>(HttpStatus.NOT_FOUND)
     */
    @GetMapping("/randommoviewithgenreandyears/{genreName}/{yearFirst}_{yearSecond}")
    public ResponseEntity<Movie> getMovieWithGenreAndYears(@PathVariable(name = "genreName") String genreName,@PathVariable(name = "yearFirst") int firstYear,@PathVariable(name = "yearSecond") int secondYear){
        Movie movie = movieDAO.getRandomMovie(new MovieGenre(genreName),firstYear,secondYear);
        return movie == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(movie,HttpStatus.OK);
    }

    /**
     * Get метод получения рандомной книги с заданным жанром по мэппингу /randombookwithgenre/bookGenre
     * @param bookGenre жанр книги {@link com.rgmb.generator.entity.Genre}
     * @return new ResponseEntity<>(book,HttpStatus.OK) or ResponseEntity<>(HttpStatus.NOT_FOUND)
     */
    @GetMapping("/randombookwithgenre/{bookGenre}")
    public ResponseEntity<Book> getRandomBookWithGenre(@PathVariable(name = "bookGenre") String bookGenre){
        Book book = bookDao.getRandomBook(new Genre(bookGenre));
        return book == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(book,HttpStatus.OK);
    }

    /**
     * Get метод получения всех жанров книг по мэппингу /getallbookgenres
     * @return ResponseEntity<>(localList,HttpStatus.OK)
     */
    @GetMapping("/getallbookgenres")
    public ResponseEntity<List<Genre>> getAllBookGenres(){
        List<Genre> localList = bookGenreDAO.findAll();
        return new ResponseEntity<>(localList,HttpStatus.OK);
    }

    /**
     * Get метод получения минимального и максимального рейтингов книг из базы данных по мэппингу /getminmaxratingbooks
     * @return ResponseEntity<>(localList,HttpStatus.OK)
     */
    @GetMapping("/getminmaxratingbooks")
    public ResponseEntity<List<Double>> getMinMaxRatingBooks(){
        double minRating = bookDao.getMinRating();
        double maxRating = bookDao.getMaxRating();
        List<Double> localList = new ArrayList<>(2);
        localList.add(minRating); localList.add(maxRating);
        return new ResponseEntity<>(localList,HttpStatus.OK);
    }

    /**
     * Get метод получения рандомной книги с указанным рейтингом по мэппингу /randombookwithrating/firstRating_secondRating
     * @param firstRating левая граница отрезка
     * @param secondRating правая граница отрезка
     * @return ResponseEntity<>(book,HttpStatus.OK) or ResponseEntity<>(HttpStatus.NOT_FOUND)
     */
    @GetMapping("/randombookwithrating/{firstRating}_{secondRating}")
    public ResponseEntity<Book> getRandomBookWithRating(@PathVariable(name = "firstRating") double firstRating, @PathVariable(name = "secondRating") double secondRating){
        Book book = bookDao.getRandomBook(firstRating,secondRating);
        return book == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(book,HttpStatus.OK);
    }

    /**
     * Get метод получения рандомной книги с указанными жанром и рейтингом по мэппингу /randombookwithgenreandrating/genre/firstRating_secondRating
     * @param genre жанр книги {@link com.rgmb.generator.entity.Genre}
     * @param firstRating левая граница отрезка
     * @param secondRating правая граница отрезка
     * @return ResponseEntity<>(book,HttpStatus.OK) or ResponseEntity<>(HttpStatus.NOT_FOUND)
     */
    @GetMapping("/randombookwithgenreandrating/{genre}/{firstRating}_{secondRating}")
    public ResponseEntity<Book> getRandomBookWithRating(@PathVariable(name = "genre") String genre,@PathVariable(name = "firstRating") double firstRating,@PathVariable(name = "secondRating") double secondRating){
        Book book = bookDao.getRandomBook(new Genre(genre),firstRating,secondRating);
        return book == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(book,HttpStatus.OK);
    }

    /**
     * Get метод получения всех жанров игр по мэппингу /getallgamegenres
     * @return ResponseEntity<>(localList,HttpStatus.OK)
     */
    @GetMapping("/getallgamegenres")
    public ResponseEntity<List<GameGenre>> getAllGameGenres(){
        List<GameGenre> localList = gameGenreDAO.findAll();
        return new ResponseEntity<>(localList,HttpStatus.OK);
    }

    /**
     * Get метод получения рандомной игры с указанным жанром по мэппингу /randomgamewithgenre/genre
     * @param genre жанр игры {@link com.rgmb.generator.entity.GameGenre}
     * @return new ResponseEntity<>(game,HttpStatus.OK) or ResponseEntity<>(HttpStatus.NOT_FOUND)
     */
    @GetMapping("/randomgamewithgenre/{genre}")
    public ResponseEntity<Game> getRandomGameWithGenre(@PathVariable(name = "genre") String genre){
        Game game = gameDAO.getRandomGame(new GameGenre(genre));
        return game == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(game,HttpStatus.OK);
    }

    /**
     * Get метод получения рандомной игры с указанными жанром и годами выпуска по мэппингу /randomgamewithgenreandyears/genre/firstYear_secondYear
     * @param genre жан игры {@link com.rgmb.generator.entity.GameGenre}
     * @param firstYear левая граница отрезка
     * @param secondYear правая граница отрезка
     * @return ResponseEntity<>(game,HttpStatus.OK) or ResponseEntity<>(HttpStatus.NOT_FOUND)
     */
    @GetMapping("/randomgamewithgenreandyears/{genre}/{firstYear}_{secondYear}")
    public ResponseEntity<Game> getRandomGameWithGenreAndYears(@PathVariable(name = "genre") String genre, @PathVariable(name = "firstYear") int firstYear,@PathVariable(name = "secondYear") int secondYear){
        Game game = gameDAO.getRandomGame(new GameGenre(genre),firstYear,secondYear);
        return game == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(game,HttpStatus.OK);
    }

    /**
     * Get метод получения минимального и максимального годов выпуска игр из базы данных по мэппингу /getminmaxyeargames
     * @return ResponseEntity<>(localList,HttpStatus.OK)
     */
    @GetMapping("/getminmaxyeargames")
    public ResponseEntity<List<Integer>> getMinMaxYearGames(){
        int minYear = gameDAO.getMinYear();
        int maxYear = gameDAO.getMaxYear();
        List<Integer> localList = new ArrayList<>();
        localList.add(minYear);localList.add(maxYear);
        return new ResponseEntity<>(localList,HttpStatus.OK);
    }

}