package com.example.standings;

import com.example.model.OperationProcessor;
import com.example.model.command.CompleteOperation;
import com.example.model.command.StartOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Beka Tsotsoria
 */
@Controller
public class AppController {

	private OperationProcessor operationProcessor;
	private StandingsProvider standingsProvider;

	@Autowired
	public AppController(OperationProcessor operationProcessor, StandingsProvider standingsProvider) {
		this.operationProcessor = operationProcessor;
		this.standingsProvider = standingsProvider;
	}

	@RequestMapping("/")
	public String index() {
		return "home";
	}

	@RequestMapping("/standings")
	public String standings(Model model) {
		model.addAttribute("standings", standingsProvider.getStandings());
		return "standings";
	}

	@RequestMapping(value = "/start", method = RequestMethod.POST)
	public void startOperation(@RequestBody StartOperation startOperation) {
		operationProcessor.process(startOperation);
	}

	@RequestMapping(value = "/complete", method = RequestMethod.POST)
	public void completeOperation(@RequestBody CompleteOperation completeOperation) {
		operationProcessor.process(completeOperation);
	}
}
