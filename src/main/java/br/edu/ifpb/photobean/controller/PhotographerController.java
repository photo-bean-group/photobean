package br.edu.ifpb.photobean.controller;

import br.edu.ifpb.photobean.service.PhotographerService;
import br.edu.ifpb.photobean.model.Photographer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/photographers")
public class PhotographerController {

    @Autowired
    private PhotographerService photographerService;

    @RequestMapping("/form")
    public ModelAndView getForm(ModelAndView modelAndView) {
        modelAndView.setViewName("photographers/form");
        modelAndView.addObject("photographer", new Photographer());
        return modelAndView;
    }

    @PostMapping
    public ModelAndView SavePhotographer(Photographer photographer, ModelAndView modelAndView,
                                   RedirectAttributes attr) {
        String operation = (photographer.getId() == null) ? "criado" : "salvo";
        photographerService.save(photographer);
        modelAndView.setViewName("redirect:photographers");
        attr.addFlashAttribute("mensagem", "Fotógrafo " + operation + " com sucesso!");

        return modelAndView;
    }

    @GetMapping("/Criado")
    public String MostrarCadastroForms(Model model) {
        if(!model.containsAttribute("photographer")){
            model.addAttribute("photographer", new Photographer());
        }
        return "cadastro/form"; //Nome do template para o formulário
    }

    @GetMapping("/sucesso")
    public String showSuccessPage() {
        return "sucesso"; // Nome do template para a página de sucesso
    }
}
