package br.edu.ifpb.photobean.service;

import br.edu.ifpb.photobean.model.Photo;
import br.edu.ifpb.photobean.model.Photographer;
import br.edu.ifpb.photobean.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepo;

    public Photo upload(Photo photo, String fileName, byte[] bytes) throws IOException {
        photo.setImagemData(bytes);
        photoRepo.save(photo);
        return photo;
    }

    public Photo save(Photo photo) {
        return photoRepo.save(photo);
    }

}
