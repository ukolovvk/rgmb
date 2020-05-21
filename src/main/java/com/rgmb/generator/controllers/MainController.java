package com.rgmb.generator.controllers;

import com.rgmb.generator.entity.Book;
import com.rgmb.generator.entity.Game;
import com.rgmb.generator.entity.Movie;
import com.rgmb.generator.entity.MovieGenre;
import com.rgmb.generator.impdao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MainController {
    @Autowired
    @Qualifier("bookDAO")
    ImpBookDAO bookDao;

    @Autowired
    @Qualifier("MovieDAO")
    ImpMovieDAO movieDAO;

    @Autowired
    @Qualifier("ImpGameDAO")
    ImpGameDAO gameDAO;

    @Autowired
    @Qualifier("movieGenreDAO")
    ImpMovieGenreDAO movieGenreDAO;

    @GetMapping("/")
    public String hello(){
        return "Hello world";
    }

    @GetMapping("/randombook")
    public ResponseEntity<Book> getRandomBook(){
        Book book = bookDao.getRandomBook();
        return book == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(book,HttpStatus.OK);
    }

    @GetMapping("/randommovie")
    public ResponseEntity<Movie> getRandomMovie(){
        Movie movie = movieDAO.getRandomMovie();
        return movie == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(movie,HttpStatus.OK);
    }

    @GetMapping("/randomgame")
    public ResponseEntity<Game> getRandomGame(){
        Game game = gameDAO.getRandomGame();
        return game == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(game,HttpStatus.OK);
    }

    @PostMapping("/addgame")
    public ResponseEntity<?> addNewGame(@RequestBody Game game){
        int local = gameDAO.add(game);
        if(local == 1)
            return new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/addmovie")
    public ResponseEntity<?> addNewMovie(@RequestBody Movie movie){
        int local = movieDAO.add(movie);
        if(local == 1)
            return new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/addbook")
    public ResponseEntity<?> addNewBook(@RequestBody Book book){
        int local = bookDao.add(book);
        if(local == 1)
            return new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/findAllMovies")
    public ResponseEntity<List<Movie>> getAllMovies(){
        return new ResponseEntity<>(movieDAO.findAll(),HttpStatus.CREATED);
    }

    @GetMapping("/getMovie/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable(name = "id") int id){
        Movie localMovie = movieDAO.findById(id);
        return localMovie == null ?  new ResponseEntity<>(HttpStatus.NOT_FOUND) :  new ResponseEntity<>(localMovie,HttpStatus.OK);
    }

    @PostMapping("/addmanymovies")
    public ResponseEntity<?> addManyMovies(@RequestBody List<Movie> list){
        list.forEach(movie -> movieDAO.add(movie));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/addmanybooks")
    public ResponseEntity<?> addManyBooks(@RequestBody List<Book> list){
        list.forEach(book -> bookDao.add(book));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/randommoviewithgenre/{movieGenre}")
    public ResponseEntity<Movie> getRandomMovieWithGenre(@PathVariable(name = "movieGenre") String movieGenre){
        Movie movie = movieDAO.getRandomMovie(new MovieGenre(movieGenre));
        return movie == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(movie,HttpStatus.OK);
    }

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

    @GetMapping("/randommovieswithyears/{yearFirst}-{yearSecond}")
    public ResponseEntity<Movie> getRandomMovieWithYears(@PathVariable(name = "yearFirst") Integer firstYear,@PathVariable(name = "yearSecond") Integer secondYear){
        Movie movie = movieDAO.getRandomMovie(firstYear,secondYear);
        return movie == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(movie,HttpStatus.OK);
    }

    @GetMapping("/getminmaxyearsmovies")
    public ResponseEntity<List<Integer>> getMinMaxYearsMovies(){
        int minYear = movieDAO.getMinYear();
        int maxYear = movieDAO.getMaxYear();
        List<Integer> localList = new ArrayList<>(2);
        localList.add(minYear);localList.add(maxYear);
        return new ResponseEntity<>(localList, HttpStatus.OK);
    }

    @GetMapping("/randommoviewithgenreandyears/{genreName}/{yearFirst}_{yearSecond}")
    public ResponseEntity<Movie> getMovieWithGenreAndYears(@PathVariable(name = "genreName") String genreName,@PathVariable(name = "yearFirst") int firstYear,@PathVariable(name = "yearSecond") int secondYear){
        Movie movie = movieDAO.getRandomMovie(new MovieGenre(genreName),firstYear,secondYear);
        return movie == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(movie,HttpStatus.OK);
    }






}

