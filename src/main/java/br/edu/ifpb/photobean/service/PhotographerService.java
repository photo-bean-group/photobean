package br.edu.ifpb.photobean.service;

import br.edu.ifpb.photobean.Repositorios.PhotographerRepository;
import br.edu.ifpb.photobean.model.Photographer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PhotographerService implements Service<Photographer, Integer> {

    @Autowired
    private PhotographerRepository photographerRepository;

    @Override
    public List<Photographer> findAll() {
        return photographerRepository.findAll();
    }

    @Override
    public Page<Photographer> findAll(Pageable p) {
        return photographerRepository.findAll(p);
    }

    @Override
    public Photographer findById(Integer id) {
        Photographer photographer = null;
        Optional<Photographer> opPhotographer = photographerRepository.findById(id);
        if (opPhotographer.isPresent()) {
            photographer = opPhotographer.get();
        }
        return photographer;
    }

    @Override
    public Photographer save(Photographer photographer) {
        return photographerRepository.save(photographer);
    }

    public Photographer savePhotographer(Photographer photographer) {
        // Verifica se o email já está em uso
        Optional<Photographer> existingPhotographer = photographerRepository.findByEmail(photographer.getEmail());
        if (existingPhotographer.isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado!");
        }
        // Salva o fotógrafo no banco
        return photographerRepository.save(photographer);
    }
}
