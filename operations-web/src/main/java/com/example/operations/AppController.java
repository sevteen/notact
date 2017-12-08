package com.example.operations;

import com.example.model.OperationProcessor;
import com.example.model.command.CompleteOperation;
import com.example.model.command.StartOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Beka Tsotsoria
 */
@Controller
public class AppController {

    private OperationProcessor operationProcessor;

    @Value("${standings.url}")
    private String standingsUrl;

    @Autowired
    public AppController(OperationProcessor operationProcessor) {
        this.operationProcessor = operationProcessor;
    }

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("standingsUrl", standingsUrl);
        return "home";
    }

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public void startOperation(@RequestBody StartOperation startOperation) {
        operationProcessor.process(startOperation);
    }

    @RequestMapping(value = "/complete", method = RequestMethod.POST)
    @ResponseBody
    public void completeOperation(@RequestBody CompleteOperation completeOperation) {
        operationProcessor.process(completeOperation);
    }
}
