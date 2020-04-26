package com.rgmb.generator.controllers;

import com.rgmb.generator.entity.Book;
import com.rgmb.generator.entity.Game;
import com.rgmb.generator.entity.Movie;
import com.rgmb.generator.impdao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("addmovie")
    public ResponseEntity<?> addNewMovie(@RequestBody Movie movie){
        int local = movieDAO.add(movie);
        if(local == 1)
            return new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("addbook")
    public ResponseEntity<?> addNewBook(@RequestBody Book book){
        int local = bookDao.add(book);
        if(local == 1)
            return new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}

