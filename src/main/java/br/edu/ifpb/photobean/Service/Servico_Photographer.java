package br.edu.ifpb.photobean.Service;

import br.edu.ifpb.photobean.Repositorios.Repository_Photographer;
import br.edu.ifpb.photobean.model.Photographer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Servico_Photographer {

    @Autowired
    private Repository_Photographer repository_Photographer;

    public Photographer savePhotographer(Photographer photographer) {
        // Verifica se o email já está em uso
        Optional<Photographer> existingPhotographer = repository_Photographer.findByEmail(photographer.getEmail());
        if (existingPhotographer.isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado!");
        }
        // Salva o fotógrafo no banco
        return repository_Photographer.save(photographer);
    }
}
