package com.rgmb.generator.controllers;

import com.rgmb.generator.entity.*;
import com.rgmb.generator.impdao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    @Qualifier("CompanyDAO")
    ImpCompanyDAO companyDAO;

    @Autowired
    @Qualifier("ImpGameDAO")
    ImpGameDAO gameDAO;


    @GetMapping("/hello")
    public String hello(@RequestParam(name = "name",required = false,defaultValue = "World") String name, Model model){
        Game game = gameDAO.getRandomGame();
        System.out.println("Title " + game.getTitle() + " genres " + game.getGenres().toString() + " countries " + game.getCountries() + " company " + game.getCompany());
        return "hello";
    }
}

