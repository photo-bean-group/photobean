package br.edu.ifpb.photobean.service;

import br.edu.ifpb.photobean.model.Photo;
import br.edu.ifpb.photobean.model.Photographer;
import br.edu.ifpb.photobean.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepo;

    private final String uploadDir = "src/main/resources/static/uploads/photos";

    public Photo upload(Photo photo, String fileName, byte[] bytes) throws IOException, IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            fos.write(bytes);
        }

        photo.setImageUrl("/uploads/photos/" + fileName);

        return photoRepo.save(photo);
    }

    public Photo save(Photo photo) {
        return photoRepo.save(photo);
    }

}
