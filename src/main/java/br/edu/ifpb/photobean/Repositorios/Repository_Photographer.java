package br.edu.ifpb.photobean.Repositorios;

import br.edu.ifpb.photobean.model.Photographer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Repository_Photographer extends JpaRepository<Photographer, Long> {
    Optional<Photographer> findByEmail(String email);
}
