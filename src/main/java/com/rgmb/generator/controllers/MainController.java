package com.rgmb.generator.controllers;

import com.rgmb.generator.entity.Book;
import com.rgmb.generator.exceptions.DaoException;
import com.rgmb.generator.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Autowired
    @Qualifier("bookService")
    BookService bookService;

    @GetMapping("/hello")
    public String hello(@RequestParam(name = "name",required = false,defaultValue = "World") String name, Model model){
        Book book;
        try {
            book = bookService.getRandomBook(0,200);
            name = book.getAuthor().getName() + " " + book.getTitle() + " " + book.getGenre().getName() + " " + book.getSize();
        }catch (DaoException e){
            System.out.println("wtf");
        }
        model.addAttribute("name",name);
        return "hello";
    }
}
