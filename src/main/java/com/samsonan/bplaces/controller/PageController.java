package com.samsonan.bplaces.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PageController {

    //----------------------- Place related methods
    
    @RequestMapping(value = {"","/","/map"}, method = RequestMethod.GET)
    public String getIndexPage(Model model) {
        return "map";
    }

    //----------------------- User related methods
    
    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String login() {
        return "login";
    }    
    
}
