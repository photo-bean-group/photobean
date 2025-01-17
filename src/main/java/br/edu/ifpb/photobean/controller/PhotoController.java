package br.edu.ifpb.photobean.controller;

import br.edu.ifpb.photobean.model.Photo;
import br.edu.ifpb.photobean.model.Photographer;
import br.edu.ifpb.photobean.service.PhotoService;
import br.edu.ifpb.photobean.service.PhotographerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
            Photo photo = photoService.upload(p, fileName, file.getBytes());
            Integer photographerId = photo.getPhotographer().getId();
            photo.setImageUrl(this.buildUrl(photographerId, photo.getId()));
            photoService.save(photo);
            System.out.println(this.buildUrl(photographerId, photo.getId()));
            message = "Foto carregada com sucesso: " + file.getOriginalFilename();
            nextPage = String.format("redirect:/photographers/%s/photos", photographerId.toString());
        } catch (Exception e) {
            message = "Não foi possível carregar o documento: " + file.getOriginalFilename() + "! " + e.getMessage();
            nextPage = "/photos/form";
        }
        mav.addObject("message", message);
        mav.setViewName(nextPage);
        return mav;

    }

    private String buildUrl(Integer photographerId, Integer photoId) {
        String fileDownloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/photographers/")
                .path(String.valueOf(photographerId))
                .path("/photos/")
                .path(String.valueOf(photoId))
                .toUriString();
        return fileDownloadUri;
    }
}
