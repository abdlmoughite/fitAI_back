package com.hessati.hessati.controllers;

import com.hessati.hessati.dto.HiringDTO;
import com.hessati.hessati.entities.Hiring;
import com.hessati.hessati.services.HiringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hiring")
public class HiringController {

    @Autowired
    private HiringService hiringService;

    @PostMapping
    public ResponseEntity<Hiring> saveHiring(@RequestBody HiringDTO hiringDTO) {
        Hiring savedHiring = hiringService.saveHiring(hiringDTO);
        return new ResponseEntity<>(savedHiring, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hiring> getHiringById(@PathVariable Long id) {
        Hiring hiring = hiringService.getHiringById(id);
        if (hiring != null) {
            return new ResponseEntity<>(hiring, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<Hiring>> getAllHirings() {
        List<Hiring> hirings = hiringService.getAllHirings();
        return new ResponseEntity<>(hirings, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hiring> updateHiring(@PathVariable Long id, @RequestBody HiringDTO hiringDTO) {
        Hiring updatedHiring = hiringService.updateHiring(id, hiringDTO);
        if (updatedHiring != null) {
            return new ResponseEntity<>(updatedHiring, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteHiring(@PathVariable Long id) {
        boolean isDeleted = hiringService.deleteHiring(id);
        return new ResponseEntity<>(isDeleted, isDeleted ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
