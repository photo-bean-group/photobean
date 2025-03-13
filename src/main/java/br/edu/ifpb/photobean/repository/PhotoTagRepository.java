package br.edu.ifpb.photobean.repository;

import br.edu.ifpb.photobean.model.PhotoTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoTagRepository extends JpaRepository<PhotoTag, Integer> {

}

