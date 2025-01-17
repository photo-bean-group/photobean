package br.edu.ifpb.photobean.repository;

import br.edu.ifpb.photobean.model.Like;
import br.edu.ifpb.photobean.model.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, LikeId> {

    boolean existsByPhotoIdAndPhotographerId(Integer photoId, Integer photographerId);

    void deleteByPhotoIdAndPhotographerId(Integer photoId, Integer photographerId);
}
