package br.edu.ifpb.photobean.controller;

import br.edu.ifpb.photobean.model.*;

import br.edu.ifpb.photobean.repository.PhotographerRepository;
import br.edu.ifpb.photobean.service.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/photographers")
@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
public class PhotographerController {

    @Autowired
    private PhotographerService photographerService;

    @Autowired
    private FollowService followService;

    @Autowired
    private TagService tagService;

    @Autowired
    private PhotoTagService photoTagService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/my-profile")
    public ModelAndView getPhotographerLogged(ModelAndView modelAndView, Principal principal) {
        Photographer photographer = photographerService.findByUsername(principal.getName());

        List<Photo> photos = photographer.getPhotos().stream()
                .sorted(Comparator.comparing(Photo::getId).reversed())
                .collect(Collectors.toList());

        boolean isFollowing = followService.findByFollowerAndFollowee(photographer, photographer) != null;
        long followersCount = followService.countByFollowee(photographer);
        long followingCount = followService.countByFollower(photographer);

        modelAndView.setViewName("photographers/details");
        modelAndView.addObject("photographer", photographer);
        modelAndView.addObject("photos", photos);
        modelAndView.addObject("isFollowing", isFollowing);
        modelAndView.addObject("canFollow", !Objects.equals(photographer.getUser().getUsername(), photographer.getUser().getUsername()));
        modelAndView.addObject("followersCount", followersCount);
        modelAndView.addObject("followingCount", followingCount);
        return modelAndView;
    }

    @GetMapping("/{id}/photos")
    public ModelAndView getPhotographer(@PathVariable Integer id, ModelAndView modelAndView, Principal principal) {
        Photographer loggedPhotographer = photographerService.findByUsername(principal.getName());
        Photographer photographer = photographerService.findById(id);

        if (photographer == null) {
            throw new IllegalArgumentException("Fotógrafo não econtrado com o ID" + id);
        }

        List<Photo> photos = photographer.getPhotos().stream()
                .sorted(Comparator.comparing(Photo::getId).reversed())
                .collect(Collectors.toList());

        boolean isFollowing = followService.findByFollowerAndFollowee(loggedPhotographer, photographer) != null;
        long followersCount = followService.countByFollowee(photographer);
        long followingCount = followService.countByFollower(photographer);

        modelAndView.setViewName("photographers/details");
        modelAndView.addObject("photographer", photographer);
        modelAndView.addObject("photos", photos);
        modelAndView.addObject("canFollow", !Objects.equals(loggedPhotographer.getUser().getUsername(), photographer.getUser().getUsername()));
        modelAndView.addObject("isFollowing", isFollowing);
        modelAndView.addObject("followersCount", followersCount);
        modelAndView.addObject("followingCount", followingCount);
        return modelAndView;
    }

    @PostMapping("{id}/follow")
    public String followPhotographer(@PathVariable Integer id, Principal principal) {
        Photographer photographerFollower = photographerService.findByUsername(principal.getName());
        Photographer photographerFollowee = photographerService.findById(id);

        if (photographerFollowee == null) {
            throw new IllegalArgumentException("Fotógrafo não econtrado com o ID" + id);
        }

        followService.save(photographerFollower, photographerFollowee);
        return "redirect:/photographers/" + id + "/photos";
    }

    @PostMapping("{id}/unfollow")
    public String unfollowPhotographer(@PathVariable Integer id, Principal principal) {
        Photographer photographerFollower = photographerService.findByUsername(principal.getName());
        Photographer photographerFollowee = photographerService.findById(id);

        if (photographerFollowee == null) {
            throw new IllegalArgumentException("Fotógrafo não econtrado com o ID" + id);
        }

        followService.delete(photographerFollower, photographerFollowee);
        return "redirect:/photographers/" + id + "/photos";
    }

    @GetMapping("{id}/photos/{photoId}")
    public ModelAndView showPhotoDetails(@PathVariable Integer id,
                                         @PathVariable Integer photoId,
                                         ModelAndView modelAndView,
                                         Principal principal) {

        Photographer photographer = photographerService.findById(id);
        Photographer loggedPhotographer = photographerService.findByUsername(principal.getName());

        if (photographer == null) {
            throw new IllegalArgumentException("Fotógrafo não encontrado com o ID " + id);
        }

        Photo photo = photographer.getPhotos().stream()
                .filter(p -> p.getId().equals(photoId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Foto não encontrada"));

        // Ordena os comentários pela data de criação (mais antigo primeiro)
        List<Comment> sortedComments = photo.getComments().stream()
                .sorted(Comparator.comparing(Comment::getCreateAt))
                .collect(Collectors.toList());

        Set<Tag> tags = photo.getTags();

        modelAndView.setViewName("photos/details");
        modelAndView.addObject("photo", photo);
        modelAndView.addObject("photographer", photographer);
        modelAndView.addObject("loggedPhotographer", loggedPhotographer);
        modelAndView.addObject("comments", sortedComments); // Lista ordenada aqui
        modelAndView.addObject("tags", tags);

        return modelAndView;
    }


    @PostMapping("{id}/photos/{photoId}/comments")
    public String addComment(@PathVariable Integer id, @PathVariable Integer photoId,
                             @RequestParam String comment, Principal principal) {
        Photographer photographer = photographerService.findByUsername(principal.getName());

        if (photographer == null) {
            throw new IllegalArgumentException("Fotógrafo não econtrado com o ID" + id);
        }

        Photo photo = photoService.findById(photoId);

        if (photo == null) {
            throw new IllegalArgumentException("Foto não econtrado com o ID" + photoId);
        }

        commentService.saveComment(comment, photo, photographer);

        return "redirect:/photographers/" + id + "/photos/" + photoId;
    }

    @GetMapping("/list")
    public String PhotographerList(Model model) {
        List<Photographer> photographerlist = photographerService.findAll();
        photographerlist.sort(Comparator.comparing(Photographer::getName));
        model.addAttribute("photographer", photographerlist);
        return "photographers/list";
    }



    @GetMapping("/feed")
    public String PhotographerListFeed(Model model) {
        List<Photographer> photographerList = photographerService.findAll();

        // Ordenando as fotos de cada fotógrafo
        for (Photographer photographer : photographerList) {
            // Convertendo o Set de fotos para List e ordenando
            List<Photo> sortedPhotos = photographer.getPhotos().stream()
                    .sorted(Comparator.comparing(Photo::getId).reversed())
                    .collect(Collectors.toList());

            // Convertendo de volta para Set (não mantém a ordem)
            Set<Photo> sortedPhotoSet = new HashSet<>(sortedPhotos);

            // Atualizando o Set de fotos com as fotos ordenadas
            photographer.setPhotos(sortedPhotoSet);
        }
        // Adicionando a lista de fotógrafos no modelo
        model.addAttribute("photographers", photographerList);

        // Retorna a página "feed" com os fotógrafos e suas fotos ordenadas
        return "photographers/feed";
    }




    @GetMapping("/Criado")
    public String MostrarCadastroForms(Model model) {
        if(!model.containsAttribute("photographer")){
            model.addAttribute("photographer", new Photographer());
        }
        return "cadastro/form";
    }

    @GetMapping("/sucesso")
    public String showSuccessPage() {
        return "sucesso";
    }

    @PostMapping("/{id}/suspend")
    public String suspendPhotographer(@PathVariable Integer id, RedirectAttributes attributes) {
        try {
            photographerService.suspendPhotographer(id);
            attributes.addFlashAttribute("message", "Fotógrafo suspenso com sucesso!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/photographers/list";
    }

    @PostMapping("/{id}/reactivate")
    public String reactivatePhotographer(@PathVariable Integer id, RedirectAttributes attributes) {
        try {
            photographerService.reactivatePhotographer(id);
            attributes.addFlashAttribute("message", "Fotógrafo reativado com sucesso!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/photographers/list";
    }

    @PostMapping("{id}/photos/{photoId}/comments/{commentId}/edit")
    public String editComment(@PathVariable Integer id,
                              @PathVariable Integer photoId,
                              @PathVariable Integer commentId,
                              @RequestParam String newText,
                              Principal principal,
                              RedirectAttributes attr) {

        Photographer photographer = photographerService.findByUsername(principal.getName());

        try {
            if (newText == null || newText.trim().isEmpty()) {
                commentService.deleteComment(commentId, photographer);
                attr.addFlashAttribute("message", "Comentário vazio removido com sucesso.");
            } else {
                commentService.editComment(commentId, newText, photographer);
                attr.addFlashAttribute("message", "Comentário alterado com sucesso.");
            }
        } catch (Exception e) {
            attr.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/photographers/" + id + "/photos/" + photoId;
    }

    @PostMapping("{id}/photos/{photoId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Integer id,
                                @PathVariable Integer photoId,
                                @PathVariable Integer commentId,
                                Principal principal,
                                RedirectAttributes attr) {
        Photographer photographer = photographerService.findByUsername(principal.getName());

        try {
            commentService.deleteComment(commentId, photographer);
            attr.addFlashAttribute("message", "Comentário excluído com sucesso.");
        } catch (Exception e) {
            attr.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/photographers/" + id + "/photos/" + photoId;
    }


    @GetMapping("/{id}/photos/{photoId}/comments/pdf")
    @PreAuthorize("hasRole('ADMIN')")
    public void generateCommentsPdf(@PathVariable Integer id,
                                    @PathVariable Integer photoId,
                                    HttpServletResponse response) {

        try {
            Photographer photographer = photographerService.findById(id);
            Photo photo = photographer.getPhotos().stream()
                    .filter(p -> p.getId().equals(photoId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Foto não encontrada"));

            List<Comment> sortedComments = photo.getComments().stream()
                    .sorted(Comparator.comparing(Comment::getCreateAt))
                    .collect(Collectors.toList());

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"comentarios-foto-" + photoId + ".pdf\"");

            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());

            document.open();
            document.add(new Paragraph("Comentários da Foto ID: " + photoId));
            document.add(new Paragraph("Fotógrafo: " + photographer.getName()));
            document.add(new Paragraph(" "));

            for (Comment comment : sortedComments) {
                document.add(new Paragraph(
                        comment.getCreateAt().toString() + " - " +
                                comment.getPhotographer().getName() + ": " +
                                comment.getCommentText()
                ));
            }

            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF: " + e.getMessage(), e);
        }
    }

    @PostMapping("{id}/photos/{photoId}/add-hashtag")
    public String addHashtagToPhoto(@PathVariable Integer id,
                                    @PathVariable Integer photoId,
                                    @RequestParam String hashtag,
                                    Principal principal,
                                    RedirectAttributes attributes) {
        // Recuperar o fotógrafo logado
        Photographer photographer = photographerService.findByUsername(principal.getName());

        // Recuperar a foto associada ao fotógrafo
        Photo photo = photoService.findById(photoId);

        Tag tag = tagService.findByTagName(hashtag);
        if (tag == null) {
            tag = new Tag();
            tag.setTagName(hashtag);
            tag = tagService.save(tag);
        }

        PhotoTag photoTag = new PhotoTag();
        photoTag.setPhoto(photo);
        photoTag.setTag(tag);
        photoTagService.save(photoTag);

        attributes.addFlashAttribute("message", "Hashtag adicionada com sucesso!");

        return "redirect:/photographers/" + id + "/photos/" + photoId;
    }

    @GetMapping("/search/tags")
    @ResponseBody
    public List<Tag> searchTags(@RequestParam String query) {
        return tagService.searchTags(query); // Chama o serviço que já implementa a lógica de busca
    }

}

