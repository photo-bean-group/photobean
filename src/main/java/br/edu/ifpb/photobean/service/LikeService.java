package br.edu.ifpb.photobean.service;

import br.edu.ifpb.photobean.model.Like;
import br.edu.ifpb.photobean.model.Photo;
import br.edu.ifpb.photobean.model.Photographer;
import br.edu.ifpb.photobean.repository.LikeRepository;
import br.edu.ifpb.photobean.repository.PhotoRepository;
import br.edu.ifpb.photobean.repository.PhotographerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PhotographerRepository photographerRepository;

    @Transactional
    public Integer toggleLike(Integer photoId, Integer photographerId) {
        Photographer photographer = photographerRepository.findById(photographerId)
                .orElseThrow(() -> new RuntimeException("Fotógrafo não encontrado"));

        if (photographer.isSuspended()) {
            throw new IllegalStateException("Operação não permitida: fotógrafo suspenso.");
        }

        boolean alreadyLiked = likeRepository.existsByPhotoIdAndPhotographerId(photoId, photographerId);
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada"));
        if (alreadyLiked) {
            likeRepository.deleteByPhotoIdAndPhotographerId(photoId, photographerId);
        } else {
            Like like = new Like();
            like.setPhoto(photo);
            like.setPhotographer(photographer);
            likeRepository.save(like);
        }

        return photo.getPhotographer().getId();
    }


    public boolean isPhotoLikedByPhotographer(Integer photoId, Integer photographerId) {
        return likeRepository.existsByPhotoIdAndPhotographerId(photoId, photographerId);
    }
}
