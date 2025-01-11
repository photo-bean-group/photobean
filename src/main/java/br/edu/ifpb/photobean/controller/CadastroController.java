package br.edu.ifpb.photobean.controller;

import br.edu.ifpb.photobean.Service.Servico_Photographer;
import br.edu.ifpb.photobean.model.Photographer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/Cadastro")
public class CadastroController {

    @Autowired
    private Servico_Photographer servico_Photographer;

    @PostMapping("/Criar")
    public String CriarCadastro(Photographer photographer, RedirectAttributes redirectAttributes) {
        try{
            Photographer savedPhotographer = servico_Photographer.savePhotographer(photographer);
            //Adiciona mensagem de sucesso para a próxima requisição
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Cadastrado com sucesso!");
            return "redirect:/Cadastro/Criar";
        }catch (IllegalArgumentException e){
            //Adiciona mensagem de erro para a próxima requisição
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/Cadastro/Criar";
        }
    }

  //  @GetMapping("/Criar")
   // public String MostrarCadastroForms(Model model) {
   //     if(!model.containsAttribute("photographer")){
   //         model.addAttribute("photographer", new Photographer());
   //     }
   //     return "CadastroForms"; //Nome do template para o formulário
   // }
   // @GetMapping("/sucesso")
   // public String showSuccessPage() {
   //     return "sucesso"; // Nome do template para a página de sucesso
   // }
}
