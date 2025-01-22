package br.edu.ifpb.photobean.service;

import br.edu.ifpb.photobean.model.Follow;
import br.edu.ifpb.photobean.model.Photographer;
import br.edu.ifpb.photobean.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowService {
    @Autowired
    private FollowRepository followRepository;

    public Follow findByFollowerAndFollowee(Photographer follower, Photographer folowee) {
        return followRepository.findByFollowerAndFollowee(follower, folowee);
    }

    public long countByFollower(Photographer photographer) {
        return followRepository.countByFollower(photographer);
    }

    public long countByFollowee(Photographer photographer) {
        return followRepository.countByFollowee(photographer);
    }

    public Follow save(Photographer follower, Photographer followee) {
        if (follower.isSuspended()) {
            throw new IllegalStateException("Operação não permitida: fotógrafo suspenso.");
        }

        Follow follow = followRepository.findByFollowerAndFollowee(follower, followee);
        if (follow != null) {
            throw new IllegalArgumentException("Já está seguindo esse fotógrafo");
        } else {
            follow = new Follow();
            follow.setFollowee(followee);
            follow.setFollower(follower);

            return followRepository.save(follow);
        }
    }


    public void delete(Photographer follower, Photographer followee) {
        Follow follow = followRepository.findByFollowerAndFollowee(follower, followee);
        if (follow != null) {
            followRepository.delete(follow);
        } else {
            throw new IllegalArgumentException("Não segue esse fotógrafo");
        }
    }
}
