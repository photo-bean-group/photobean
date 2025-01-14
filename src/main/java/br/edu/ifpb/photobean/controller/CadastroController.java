package br.edu.ifpb.photobean.controller;

import br.edu.ifpb.photobean.Service.Servico_Photographer;
import br.edu.ifpb.photobean.model.Photo;
import br.edu.ifpb.photobean.model.Photographer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/Cadastro")
public class CadastroController {

    @Autowired
    private Servico_Photographer servico_Photographer;

    @RequestMapping("/form")
    public ModelAndView getForm(ModelAndView modelAndView) {
        modelAndView.setViewName("cadastro/form");
        modelAndView.addObject("photographer", new Photographer());
        return modelAndView;
    }

    @PostMapping("/Criar")
    public String CriarCadastro(Photographer photographer, RedirectAttributes redirectAttributes) {
        try{
            Photographer savedPhotographer = servico_Photographer.savePhotographer(photographer);
            //Adiciona mensagem de sucesso para a próxima requisição
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Cadastrado com sucesso!");
            return "redirect:/Cadastro/Criado";
        }catch (IllegalArgumentException e){
            //Adiciona mensagem de erro para a próxima requisição
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/Cadastro/Criar";
        }
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
