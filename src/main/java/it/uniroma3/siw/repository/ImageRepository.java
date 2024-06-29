package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Image;

public interface ImageRepository extends CrudRepository<Image,Long> {

}