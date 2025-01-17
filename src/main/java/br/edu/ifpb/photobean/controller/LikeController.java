package br.edu.ifpb.photobean.controller;

import br.edu.ifpb.photobean.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/photos")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/{photoId}/like-toggle")
    public String toggleLike(@PathVariable Integer photoId, @RequestParam Integer photographerId) {
        likeService.toggleLike(photoId, photographerId);
        return "redirect:/photos/" + photoId;
    }
}
