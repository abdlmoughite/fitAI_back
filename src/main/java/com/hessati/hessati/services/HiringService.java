package com.hessati.hessati.services;

import com.hessati.hessati.dto.HiringDTO;
import com.hessati.hessati.entities.Hiring;
import com.hessati.hessati.repositories.HiringRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HiringService {

    @Autowired
    private HiringRepository hiringRepository;

    public Hiring saveHiring(HiringDTO hiringDTO) {
        Hiring hiring = new Hiring();
        hiring.setEmail(hiringDTO.getEmail());
        hiring.setFirstName(hiringDTO.getFirstName());
        hiring.setLastName(hiringDTO.getLastName());
        hiring.setAddress(hiringDTO.getAddress());
        hiring.setCodePostal(hiringDTO.getCodePostal());
        hiring.setCity(hiringDTO.getCity());
        hiring.setPays(hiringDTO.getPays());
        hiring.setTel(hiringDTO.getTel());
        hiring.setCvUrl(hiringDTO.getCvUrl());
        hiring.setDescription(hiringDTO.getDescription());
        return hiringRepository.save(hiring);
    }

    public Hiring getHiringById(Long id) {
        Optional<Hiring> optionalHiring = hiringRepository.findById(id);
        return optionalHiring.orElse(null);
    }

    public List<Hiring> getAllHirings() {
        return hiringRepository.findAll();
    }

    public Hiring updateHiring(Long id, HiringDTO hiringDTO) {
        Optional<Hiring> optionalHiring = hiringRepository.findById(id);
        if (optionalHiring.isPresent()) {
            Hiring existingHiring = optionalHiring.get();
            existingHiring.setEmail(hiringDTO.getEmail());
            existingHiring.setFirstName(hiringDTO.getFirstName());
            existingHiring.setLastName(hiringDTO.getLastName());
            existingHiring.setAddress(hiringDTO.getAddress());
            existingHiring.setCodePostal(hiringDTO.getCodePostal());
            existingHiring.setCity(hiringDTO.getCity());
            existingHiring.setPays(hiringDTO.getPays());
            existingHiring.setTel(hiringDTO.getTel());
            existingHiring.setCvUrl(hiringDTO.getCvUrl());
            existingHiring.setDescription(hiringDTO.getDescription());
            return hiringRepository.save(existingHiring);
        }
        return null;
    }

    public boolean deleteHiring(Long id) {
        if (hiringRepository.existsById(id)) {
            hiringRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
