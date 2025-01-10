package br.edu.ifpb.photobean.controller;

import br.edu.ifpb.photobean.model.Photo;
import br.edu.ifpb.photobean.model.Photographer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/photos")
public class PhotoController {

    @RequestMapping("/form")
    public ModelAndView getForm(ModelAndView modelAndView) {
        modelAndView.setViewName("photos/form");
        modelAndView.addObject("photo", new Photo(new Photographer()));
        return modelAndView;
    }
}
