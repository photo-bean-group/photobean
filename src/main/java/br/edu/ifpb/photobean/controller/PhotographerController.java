package br.edu.ifpb.photobean.controller;

import br.edu.ifpb.photobean.model.Comment;
import br.edu.ifpb.photobean.model.Photo;
import br.edu.ifpb.photobean.service.CommentService;
import br.edu.ifpb.photobean.service.PhotoService;
import br.edu.ifpb.photobean.service.PhotographerService;
import br.edu.ifpb.photobean.model.Photographer;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/photographers")
public class PhotographerController {

    @Autowired
    private PhotographerService photographerService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private CommentService commentService;

    @RequestMapping("/form")
    public ModelAndView getForm(ModelAndView modelAndView) {
        modelAndView.setViewName("photographers/form");
        modelAndView.addObject("photographer", new Photographer());
        return modelAndView;
    }

    @PostMapping
    public ModelAndView SavePhotographer(@Valid Photographer photographer, ModelAndView modelAndView,
                                         BindingResult validation, RedirectAttributes attr) {
        if (validation.hasErrors()) {
            modelAndView.setViewName("contas/form");
            return modelAndView;
        }
        String operation = (photographer.getId() == null) ? "criado" : "salvo";
        photographerService.save(photographer);
        modelAndView.setViewName("redirect:photographers");
        attr.addFlashAttribute("mensagem", "Fotógrafo " + operation + " com sucesso!");

        return modelAndView;
    }

    @GetMapping("/{id}/photos")
    public ModelAndView getPhotographers(@PathVariable Integer id, ModelAndView modelAndView) {
        Photographer photographer = photographerService.findById(id);

        if (photographer == null) {
            throw new IllegalArgumentException("Fotografo não econtrado com o ID" + id);
        }

        modelAndView.setViewName("photographers/details");
        modelAndView.addObject("photographer", photographer);
        modelAndView.addObject("photo");
        return modelAndView;
    }

    @GetMapping("{id}/photos/{photoId}")
    public ModelAndView showPhotoDetails(@PathVariable Integer id, @PathVariable Integer photoId,
                                         ModelAndView modelAndView) {
        Photographer photographer = photographerService.findById(id);
        Photo photo = photographer.getPhotos().stream()
                .filter(p -> p.getId().equals(photoId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Foto não encontrada"));

        modelAndView.setViewName("photos/details");
        modelAndView.addObject("photo", photo);
        modelAndView.addObject("photographer", photographer);
        return modelAndView;
    }

    @PostMapping("{id}/photos/{photoId}/comments")
    public String addComment(@PathVariable Integer id, @PathVariable Integer photoId,
                             @RequestParam String comment) {

        Photographer photographer = photographerService.findById(id);
        Photo photo = photoService.findById(photoId);


        // Cria e configura o comentário
        Comment comments = new Comment();
        comments.setPhoto(photo);
        comments.setPhotographer(photographer);
        comments.setCommentText(comment);
        comments.setCreateAt(LocalDateTime.now());

        // Salva o comentário
        commentService.addComment(comments);

        // Redireciona para a página de detalhes da foto
        return "redirect:/photographers/" + id + "/photos/" + photoId;
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
