package com.springapp.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
/**
 * Created by WU on 14/3/2015.
 */
@Controller
@RequestMapping("/")
public class MvcController {
    @RequestMapping(method = RequestMethod.GET)
    public String sayHi(Model model) {
        System.out.println("********");
        return "helloworld";
    }
}

