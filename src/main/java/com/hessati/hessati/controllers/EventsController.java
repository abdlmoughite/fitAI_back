package com.hessati.hessati.controllers;

import com.hessati.hessati.dto.EventsDTO;
import com.hessati.hessati.dto.ProductDTO;
import com.hessati.hessati.entities.Events;
import com.hessati.hessati.entities.Product;
import com.hessati.hessati.services.EventsService;
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
@RequestMapping("/api/events")
public class EventsController {

    @Autowired
    private EventsService eventsService;

    @GetMapping
    public ResponseEntity<Page<Events>> getAllProducts(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Events> events = eventsService.getAllEvents(pageable);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/last-three")
    public ResponseEntity<List<Events>> getLastThreeEvents() { // default 10 per page
        List<Events> events = eventsService.getLastThreeEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Events> saveEvent( @RequestPart("event") EventsDTO event,
                                             @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        Events savedEvent = eventsService.saveEvent(event, images);
        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Events> getEventById(@PathVariable Long id) {
        Events event = eventsService.getEventById(id);
        if (event != null) {
            return new ResponseEntity<>(event, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Events> updateEvent( @RequestPart("event") EventsDTO event,
                                             @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        Events savedEvent = eventsService.updateEvent(event.getId(), event, images);
        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteEvent(@PathVariable Long id) {
        boolean isDeleted = eventsService.deleteEvent(id);
        return new ResponseEntity<>(isDeleted, isDeleted ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
