package br.edu.ifpb.photobean.repository;

import br.edu.ifpb.photobean.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    Tag findByTagName(String tagName);

    @Query("SELECT t FROM Tag t WHERE t.tagName LIKE %:query%")
    List<Tag> findByTagNameContaining(@Param("query") String query); // Busca tags pelo nome parcial
}
