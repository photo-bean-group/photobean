package br.edu.ifpb.photobean.repository;

import br.edu.ifpb.photobean.model.Photographer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhotographerRepository extends JpaRepository<Photographer, Integer> {
    Optional<Photographer> findByEmail(String email);

    Photographer findFirstByOrderByIdDesc();

    @Query("from Photographer p join fetch p.user u where u.username = :username")
    Photographer findByUsername(@Param("username") String username);
}
