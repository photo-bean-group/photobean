package br.edu.ifpb.photobean.service;

import br.edu.ifpb.photobean.model.Comment;
import br.edu.ifpb.photobean.model.Photo;
import br.edu.ifpb.photobean.model.Photographer;
import br.edu.ifpb.photobean.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Comment saveComment(String commentText, Photo photo, Photographer photographer) {
        if (photographer.isSuspended()) {
            throw new IllegalStateException("Operação não permitida: fotógrafo suspenso.");
        }

        Comment comment = new Comment();
        comment.setPhoto(photo);
        comment.setPhotographer(photographer);
        comment.setCommentText(commentText);
        comment.setCreateAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public Comment editComment(Integer commentId, String newText, Photographer photographer) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comentário não encontrado com o ID: " + commentId));

        if (!comment.getPhotographer().equals(photographer)) {
            throw new IllegalStateException("Operação não permitida: comentário não pertence ao fotógrafo.");
        }

        comment.setCommentText(newText);
        // Data permanece inalterada
        return commentRepository.save(comment);
    }

    public void deleteComment(Integer commentId, Photographer photographer) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comentário não encontrado com o ID: " + commentId));

        if (!comment.getPhotographer().equals(photographer)) {
            throw new IllegalStateException("Operação não permitida: comentário não pertence ao fotógrafo.");
        }

        commentRepository.delete(comment);
    }

}
