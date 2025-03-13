package br.edu.ifpb.photobean.service;

import br.edu.ifpb.photobean.model.PhotoTag;
import br.edu.ifpb.photobean.repository.PhotoTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoTagService {

    @Autowired
    private PhotoTagRepository photoTagRepository;

    public PhotoTag save(PhotoTag photoTag) {
        return photoTagRepository.save(photoTag); // Salva a associação entre foto e tag
    }
}

