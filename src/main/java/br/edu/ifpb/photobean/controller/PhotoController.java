package br.edu.ifpb.photobean.controller;

import br.edu.ifpb.photobean.DTO.PhotoFeedDTO;
import br.edu.ifpb.photobean.model.Photo;
import br.edu.ifpb.photobean.model.Photographer;
import br.edu.ifpb.photobean.service.PhotoService;
import br.edu.ifpb.photobean.service.PhotographerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Controller
@RequestMapping("/photos")
public class PhotoController {

    @Autowired
    private PhotographerService photographerService;

    @Autowired
    private PhotoService photoService;

    @RequestMapping("/form")
    public ModelAndView getForm(ModelAndView modelAndView) {
        modelAndView.setViewName("photos/form");
        modelAndView.addObject("photo", new Photo(new Photographer()));
        return modelAndView;
    }

    @ModelAttribute("photographerItems")
    public List<Photographer> getPhotogtaphers() {
        return photographerService.findAll();
    }

    @PostMapping
    public ModelAndView savePhoto(@RequestParam("file") MultipartFile file, Photo p, ModelAndView mav) {
        String message = "";
        String nextPage = "";
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Photo photo = photoService.save(p, fileName, file.getBytes());
            Integer photographerId = photo.getPhotographer().getId();
            message = "Foto carregada com sucesso: " + file.getOriginalFilename();
            nextPage = String.format("redirect:/photographers/%s/photos/%s", photographerId.toString(), photo.getId());
        } catch (Exception e) {
            message = "Não foi possível carregar o documento: " + file.getOriginalFilename() + "! " + e.getMessage();
            nextPage = "/photos/form";
        }
        mav.addObject("message", message);
        mav.setViewName(nextPage);
        return mav;

    }

    @PostMapping("/{photoId}/like")
    public ModelAndView likePhoto(@PathVariable Integer photoId,
                                  @RequestParam Integer photographerId,
                                  ModelAndView mav) {
        try {
            //photographerService.likePhoto(photoId, photographerId);
            mav.setViewName("redirect:/photos/" + photoId);
        } catch (Exception e) {
            mav.addObject("error", "Erro ao curtir a foto: " + e.getMessage());
            mav.setViewName("error");
        }
        return mav;
    }

    @PostMapping("/{photoId}/unlike")
    public ModelAndView unlikePhoto(@PathVariable Integer photoId,
                                    @RequestParam Integer photographerId,
                                    ModelAndView mav) {
        try {
            //photographerService.unlikePhoto(photoId, photographerId);
            mav.setViewName("redirect:/photos/" + photoId);
        } catch (Exception e) {
            mav.addObject("error", "Erro ao descurtir a foto: " + e.getMessage());
            mav.setViewName("error");
        }
        return mav;
    }



}
