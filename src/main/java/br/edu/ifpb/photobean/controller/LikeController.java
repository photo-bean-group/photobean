package br.edu.ifpb.photobean.controller;

import br.edu.ifpb.photobean.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/photos")
@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/{photoId}/like-toggle")
    public String toggleLike(@PathVariable Integer photoId, @RequestParam Integer loggedPhotographerId) {
        Integer photographerId = likeService.toggleLike(photoId, loggedPhotographerId);

        return "redirect:/photographers/" + photographerId + "/photos/" + photoId;
    }




}

