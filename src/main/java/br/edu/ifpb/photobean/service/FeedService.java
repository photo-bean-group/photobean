package br.edu.ifpb.photobean.service;

import br.edu.ifpb.photobean.model.Photo;
import br.edu.ifpb.photobean.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedService {
    @Autowired
    private PhotoRepository photoRepository;

    public List<Photo> getFeedPhotos() {
        return photoRepository.findAllOrderedByDate();
    }
}
