package br.edu.ifpb.photobean.controller;

import br.edu.ifpb.photobean.model.Photographer;
import br.edu.ifpb.photobean.service.PhotographerService;
import br.edu.ifpb.photobean.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private PhotographerService photographerService;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public ModelAndView getLoginForm(ModelAndView modelAndView) {
        modelAndView.setViewName("auth/login");
        return modelAndView;
    }

    @RequestMapping("/signup")
    public ModelAndView getSingupForm(ModelAndView modelAndView) {
        modelAndView.setViewName("photographers/form");
        modelAndView.addObject("photographer", new Photographer());
        return modelAndView;
    }

    @PostMapping("/signup/save")
    public ModelAndView savePhotographer(@Valid Photographer photographer, BindingResult validation,
                                         RedirectAttributes attr) {
        ModelAndView modelAndView = new ModelAndView();
        if (validation.hasErrors()) {
            modelAndView.setViewName("photographers/form");
            return modelAndView;
        }

        userService.save(photographer.getUser());
        photographerService.save(photographer);

        attr.addFlashAttribute("message", "Fotógrafo cadastrado com sucesso!");
        modelAndView.setViewName("redirect:/login");

        return modelAndView;
    }
}
