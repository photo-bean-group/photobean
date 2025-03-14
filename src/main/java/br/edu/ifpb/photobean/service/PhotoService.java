package br.edu.ifpb.photobean.service;

import br.edu.ifpb.photobean.DTO.PhotoFeedDTO;
import br.edu.ifpb.photobean.model.Photo;
import br.edu.ifpb.photobean.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepo;
    @Autowired
    private PhotoRepository photoRepository;

    private final String uploadDir = "src/main/resources/static/uploads/photos";

    public Photo save(Photo photo, String fileName, byte[] bytes) throws IOException {
        // Verifica se o fotógrafo está suspenso
        if (photo.getPhotographer().isSuspended()) {
            throw new IllegalStateException("Operação não permitida: o fotógrafo está suspenso.");
        }

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Escreve o arquivo no caminho de upload
        Path filePath = uploadPath.resolve(fileName);
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            fos.write(bytes);
        }

        // Define a URL da imagem e salva a foto no banco
        photo.setImageUrl("/uploads/photos/" + fileName);
        return photoRepo.save(photo);
    }

    public Photo findById(Integer photoId) {
        return photoRepo.findById(photoId)
                .orElseThrow(() -> new IllegalArgumentException("Foto não encontrada com o ID: " + photoId));
    }

    public List<PhotoFeedDTO> getPhotoFeed() { // Utilizado para pegar o feed de fotos
        List<Photo> photos = photoRepo.findAll();
        return photos.stream()
                .map(photo -> new PhotoFeedDTO(
                        photo.getId(),
                        photo.getImageUrl(),
                        photo.getPhotographer().getName(),
                        photo.getPhotographer().getEmail()
                ))
                .collect(Collectors.toList());
    }
    public List<Photo> getFeedPhotos() {
        return photoRepository.findAllOrderedByDate();
    }
}

