package com.hessati.hessati.services;

import com.hessati.hessati.dto.EventsDTO;
import com.hessati.hessati.entities.Events;
import com.hessati.hessati.entities.Images;
import com.hessati.hessati.entities.Product;
import com.hessati.hessati.repositories.EventsRepository;
import com.hessati.hessati.repositories.ImagesRepository;
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
public class EventsService {

    @Autowired
    private EventsRepository eventsRepository;
    @Autowired
    private ImagesRepository imagesRepository;

    public Events saveEvent(EventsDTO eventsDTO, List<MultipartFile> images) {
        Events event = new Events();
        event.setTitle(eventsDTO.getTitle());
        event.setEventDate(eventsDTO.getEventDate());
        event.setEventLocation(eventsDTO.getEventLocation());
        event.setDescription(eventsDTO.getDescription());
        return eventsRepository.save(event);
    }

    public Events updateEvent(Long id, EventsDTO eventsDTO, List<MultipartFile> images) {
        Optional<Events> optionalEvent = eventsRepository.findById(id);
        if (optionalEvent.isPresent()) {
            Events existingEvent = optionalEvent.get();
            existingEvent.setTitle(eventsDTO.getTitle());
            existingEvent.setEventDate(eventsDTO.getEventDate());
            existingEvent.setEventLocation(eventsDTO.getEventLocation());
            existingEvent.setDescription(eventsDTO.getDescription());
            return eventsRepository.save(existingEvent);
        }
        return null;
    }

    public Events getEventById(Long id) {
        Optional<Events> optionalEvent = eventsRepository.findById(id);
        return optionalEvent.orElse(null);
    }

    public Page<Events> getAllEvents(Pageable pageable) {
        Pageable sortedByCreatedAt = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("id").descending()
        );
        return eventsRepository.findAll(sortedByCreatedAt);
    }

    public List<Events> getLastThreeEvents() {
        return eventsRepository.findTop3ByOrderByIdDesc();
    }

    public boolean deleteEvent(Long id) {
        if (eventsRepository.existsById(id)) {
            eventsRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
