package com.hessati.hessati.controllers;

import com.hessati.hessati.dto.ReferencesDTO;
import com.hessati.hessati.entities.Product;
import com.hessati.hessati.entities.References;
import com.hessati.hessati.services.ReferencesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/references")
public class ReferencesController {

    @Autowired
    private ReferencesService referencesService;

    @GetMapping
    public ResponseEntity<Page<References>> getAllReferences(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<References> references = referencesService.getAllReferences(pageable);
        return new ResponseEntity<>(references, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<References>> getAllReferences() {
        List<References> references = referencesService.getListReferences();
        return new ResponseEntity<>(references, HttpStatus.OK);
    }

    @GetMapping("/last-three")
    public ResponseEntity<List<References>> getLastThreeReferences() { // default 10 per page
        List<References> references = referencesService.getLastThreeReferences();
        return new ResponseEntity<>(references, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<References> createReference(
            @RequestPart("reference") ReferencesDTO referencesDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        References createdProduct = referencesService.createReference(referencesDTO, images);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<References> getReferenceById(@PathVariable Long id) {
        References reference = referencesService.getReferenceById(id);
        return reference != null
                ? new ResponseEntity<>(reference, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping
    public ResponseEntity<References> updateReference(
            @RequestPart("reference") ReferencesDTO referencesDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        References createdProduct = referencesService.updateReference(referencesDTO.getId(), referencesDTO, images);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteReference(@PathVariable Long id) {
        boolean deleted = referencesService.deleteReference(id);
        return new ResponseEntity<>(deleted, deleted ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
