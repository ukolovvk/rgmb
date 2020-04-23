package com.rgmb.generator.controllers;

import com.rgmb.generator.entity.Actor;
import com.rgmb.generator.entity.Book;
import com.rgmb.generator.entity.Game;
import com.rgmb.generator.impdao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/")
public class MainController {
    @Autowired
    @Qualifier("bookDAO")
    ImpBookDAO bookDao;

    @Autowired
    @Qualifier("MovieDAO")
    ImpMovieDAO movieDAO;

    @Autowired
    @Qualifier("actorDAO")
    ImpActorDAO actorDAO;

    @Autowired
    @Qualifier("ProductionDAO")
    ImpProductionDAO productionDAO;

    @Autowired
    @Qualifier("CompanyDAO")
    ImpCompanyDAO companyDAO;

    @Autowired
    @Qualifier("ImpGameDAO")
    ImpGameDAO gameDAO;

    @GetMapping("/hello")
    public ResponseEntity<Game> hello(){
        return  new ResponseEntity<>(gameDAO.getRandomGame(),HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/hellopost", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> hellopost(@RequestBody Game game){
        gameDAO.add(game);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/rgmb")
    public String rgmbLook(){
        Book book = bookDao.findById(4);
        if(book == null)
            System.out.println("ok");
        return "index";
    }

}

