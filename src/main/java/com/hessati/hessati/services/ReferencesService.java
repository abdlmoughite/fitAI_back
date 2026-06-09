package com.hessati.hessati.services;

import com.hessati.hessati.dto.ReferencesDTO;
import com.hessati.hessati.entities.Images;
import com.hessati.hessati.entities.Product;
import com.hessati.hessati.entities.References;
import com.hessati.hessati.repositories.ImagesRepository;
import com.hessati.hessati.repositories.ReferencesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReferencesService{

    @Autowired
    private ReferencesRepository referencesRepository;
    @Autowired
    private ImagesRepository imagesRepository;

    public Page<References> getAllReferences(Pageable pageable) {
        Pageable sortedByCreatedAt = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("id").descending()
        );
        return referencesRepository.findAll(sortedByCreatedAt);
    }

    public List<References> getListReferences() {
        return referencesRepository.findAll();
    }

    public List<References> getLastThreeReferences() {
        return referencesRepository.findTop3ByOrderByIdDesc();
    }

    public References createReference(ReferencesDTO referencesDTO, List<MultipartFile> images) {
        References reference = new References();
        reference.setName(referencesDTO.getName());
        reference.setDescription(referencesDTO.getDescription());
        return referencesRepository.save(reference);
    }

    public References updateReference(Long id, ReferencesDTO referencesDTO, List<MultipartFile> newImages) {
        Optional<References> optionalReference = referencesRepository.findById(id);
        if (!optionalReference.isPresent()) return null;
        References reference = optionalReference.get();
        reference.setName(referencesDTO.getName());
        reference.setDescription(referencesDTO.getDescription());
        return referencesRepository.save(reference);
    }

    public References getReferenceById(Long id) {
        return referencesRepository.findById(id).orElse(null);
    }

    public boolean deleteReference(Long id) {
        if (!referencesRepository.existsById(id)) {
            return false;
        }
        referencesRepository.deleteById(id);
        return true;
    }
}
