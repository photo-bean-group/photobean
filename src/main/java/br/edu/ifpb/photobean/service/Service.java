package br.edu.ifpb.photobean.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface Service<T, ID> {

    public List<T> findAll();

    public Page<T> findAll(Pageable p);

    public T findById(ID id);

    public T save(T t);
}
