package br.edu.ifpb.photobean.repository;

import br.edu.ifpb.photobean.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {
    @Query("SELECT p FROM Photo p ORDER BY p.createdAt DESC")
    List<Photo> findAllOrderedByDate();

}
