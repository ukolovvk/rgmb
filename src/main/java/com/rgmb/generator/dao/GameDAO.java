package com.rgmb.generator.dao;

import com.rgmb.generator.entity.Country;
import com.rgmb.generator.entity.Game;
import com.rgmb.generator.entity.GameCompany;
import com.rgmb.generator.entity.GameGenre;

import java.util.List;

public interface GameDAO  extends GeneralGameDAO<Game>{

    int updateTitleById(int id,String title);

    int updateAnnotationById(int id,String annotation);

    int updateYearById(int id, int releaseYear);

    int updateCompany(int id, GameCompany company);

    int updateGameGenreById(int id, List<GameGenre> gameGenres);

    int updateCountriesById(int id, List<Country> countries);

    List<Game> findByTitle(String title);

    Game getRandomGame();

    Game getRandomGame(GameGenre genre);

    Game getRandomGame(int firstYear,int secondYear);

    Game getRandomGame(GameGenre genre, int firstYear, int secondYear);

}
