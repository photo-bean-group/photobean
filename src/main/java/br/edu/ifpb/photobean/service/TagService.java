package br.edu.ifpb.photobean.service;

import br.edu.ifpb.photobean.model.Tag;
import br.edu.ifpb.photobean.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public Tag findByTagName(String tagName) {
        return tagRepository.findByTagName(tagName); // Busca pela tag no banco
    }

    public Tag save(Tag tag) {
        return tagRepository.save(tag); // Salva a tag no banco
    }

    public List<Tag> searchTags(String query) {
        return tagRepository.findByTagNameContainingIgnoreCase(query); // Busca as tags com base no nome
    }

}

