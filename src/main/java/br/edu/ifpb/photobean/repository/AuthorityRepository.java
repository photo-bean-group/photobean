package br.edu.ifpb.photobean.repository;

import br.edu.ifpb.photobean.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Authority.AuthorityId> {
}