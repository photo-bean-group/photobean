package br.edu.ifpb.photobean.repository;

import br.edu.ifpb.photobean.model.Photographer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhotographerRepository extends JpaRepository<Photographer, Integer> {
    Optional<Photographer> findByEmail(String email);

    Photographer findFirstByOrderByIdDesc();


}
