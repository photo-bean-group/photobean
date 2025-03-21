package br.edu.ifpb.photobean.service;

import br.edu.ifpb.photobean.repository.PhotographerRepository;
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

    public Photographer findMostRecentPhotographer() {
        return photographerRepository.findFirstByOrderByIdDesc();
    }

    @Override
    public Photographer save(Photographer photographer) {
        Optional<Photographer> existingPhotographer = photographerRepository.findByEmail(photographer.getEmail());
        if (existingPhotographer.isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado!");
        }
        return photographerRepository.save(photographer);
    }

    public Photographer findByUsername(String username) {
        return photographerRepository.findByUsername(username);
    }

    public Photographer suspendPhotographer(Integer id) {
        Photographer photographer = findById(id);
        if (photographer == null) {
            throw new IllegalArgumentException("Fotógrafo não encontrado com o ID: " + id);
        }
        if (photographer.isSuspended()) {
            throw new IllegalStateException("Fotógrafo já está suspenso.");
        }
        photographer.setSuspended(true);
        return photographerRepository.save(photographer);
    }

    public Photographer reactivatePhotographer(Integer id) {
        Photographer photographer = findById(id);
        if (photographer == null) {
            throw new IllegalArgumentException("Fotógrafo não encontrado com o ID: " + id);
        }
        if (!photographer.isSuspended()) {
            throw new IllegalStateException("Fotógrafo já está ativo.");
        }
        photographer.setSuspended(false);
        return photographerRepository.save(photographer);
    }


    public Photographer suspendPhotographerFromCommenting(Integer id) {
        Photographer photographer = findById(id);
        if (photographer == null) {
            throw new IllegalArgumentException("Fotógrafo não encontrado com o ID: " + id);
        }
        if (photographer.isSuspendedFromCommenting()) {
            throw new IllegalStateException("Fotógrafo já está suspenso de comentar.");
        }

        photographer.setSuspendedFromCommenting(true);
        return photographerRepository.save(photographer);
    }
    public Photographer allowPhotographerToComment(Integer id) {
        Photographer photographer = findById(id);
        if (photographer == null) {
            throw new IllegalArgumentException("Fotógrafo não encontrado com o ID: " + id);
        }
        if (!photographer.isSuspendedFromCommenting()) {
            throw new IllegalStateException("Fotógrafo já pode comentar.");
        }
        photographer.setSuspendedFromCommenting(false);
        return photographerRepository.save(photographer);
    }






}
