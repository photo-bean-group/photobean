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

    @Transactional // Adicionando transação aqui
    public void toggleLike(Integer photoId, Integer photographerId) {
        boolean alreadyLiked = likeRepository.existsByPhotoIdAndPhotographerId(photoId, photographerId);

        if (alreadyLiked) {
            likeRepository.deleteByPhotoIdAndPhotographerId(photoId, photographerId);
        } else {
            Photo photo = photoRepository.findById(photoId)
                    .orElseThrow(() -> new RuntimeException("Foto não encontrada"));
            Photographer photographer = photographerRepository.findById(photographerId)
                    .orElseThrow(() -> new RuntimeException("Fotógrafo não encontrado"));

            Like like = new Like();
            like.setPhoto(photo);
            like.setPhotographer(photographer);
            likeRepository.save(like);
        }
    }

    public boolean isPhotoLikedByPhotographer(Integer photoId, Integer photographerId) {
        return likeRepository.existsByPhotoIdAndPhotographerId(photoId, photographerId);
    }
}
