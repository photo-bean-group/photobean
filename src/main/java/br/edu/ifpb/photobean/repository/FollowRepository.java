package br.edu.ifpb.photobean.repository;

import br.edu.ifpb.photobean.model.Follow;
import br.edu.ifpb.photobean.model.Photographer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Integer>{
    Follow findByFollowerAndFollowee(Photographer follower, Photographer followee);

    long countByFollower(Photographer photographer);

    long countByFollowee(Photographer photographer);
}
