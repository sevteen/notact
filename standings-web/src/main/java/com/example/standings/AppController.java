package com.example.standings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Beka Tsotsoria
 */
@Controller
public class AppController {

    private StandingsProvider standingsProvider;

    @Autowired
    public AppController(StandingsProvider standingsProvider) {
        this.standingsProvider = standingsProvider;
    }

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("standings", standingsProvider.getStandings());
        return "home";
    }

}
