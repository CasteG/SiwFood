package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.repository.ImageRepository;

@Controller
public class ImageController {
	
	@Autowired
	private ImageRepository imageRepository;
	
	@GetMapping("/image/{id}")
	public ResponseEntity<byte[]> showImage(@PathVariable("id") Long id) {
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        Image img = this.imageRepository.findById(id).get();
        return new ResponseEntity<>(img.getBytes(), headers, HttpStatus.OK);
	}
	

}
