package br.edu.ifpb.photobean.repository;

import br.edu.ifpb.photobean.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByPhotoId(Integer photoId);
}
