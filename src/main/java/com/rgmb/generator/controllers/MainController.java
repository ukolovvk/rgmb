package com.rgmb.generator.controllers;

import com.rgmb.generator.entity.*;
import com.rgmb.generator.impdao.ImpActorDAO;
import com.rgmb.generator.impdao.ImpBookDAO;
import com.rgmb.generator.impdao.ImpMovieDAO;
import com.rgmb.generator.impdao.ImpProductionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
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

    @GetMapping("/hello")
    public String hello(@RequestParam(name = "name",required = false,defaultValue = "World") String name, Model model){
        Book book = bookDao.getRandomBook();
        String authors = "";
        for(Author author : book.getAuthors()){
            authors += author.getName() + ", ";
        }
        String genres = "";
        for(Genre genre : book.getGenres()){
            authors += genre.getName() + ", ";
        }
        System.out.println("Title " + book.getTitle() + " authors : " + authors + " genres: " + genres + " page count = " + book.getSize() + " release year : " + book.getYear() + " rating : " + book.getRating() + " annotation : " + book.getAnnotation());
        return "hello";
    }
}

