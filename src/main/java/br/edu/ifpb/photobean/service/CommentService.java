package br.edu.ifpb.photobean.service;

import br.edu.ifpb.photobean.model.Comment;
import br.edu.ifpb.photobean.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getCommentsByPhotoId(Integer photoId) {
        return commentRepository.findByPhotoId(photoId);
    }

    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }
}
