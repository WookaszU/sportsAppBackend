package pl.edu.agh.sportsApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.sportsApp.dto.EventDTO;
import pl.edu.agh.sportsApp.dto.ResponseCode;
import pl.edu.agh.sportsApp.model.Event;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.repository.EventRepository;
import pl.edu.agh.sportsApp.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class EventService {
    private EventRepository eventRepository;
    private UserRepository userRepository;

    @Autowired
    public EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public void saveEvent(EventDTO eventDTO, User owner) {
        Event newEvent = eventDTO.parseEvent();
        newEvent.setOwnerId(owner.getId());
        newEvent.setOwner(owner);
        Set<User> participants = new HashSet<>();
        participants.add(owner);
        newEvent.setParticipants(participants);
        Set<Long> participantIds = new HashSet<>();
        participantIds.add(owner.getId());
        newEvent.setParticipantIds(participantIds);
        eventRepository.save(newEvent);
    }

    public void addParticipant(Long eventId, Long participantId) {
        Event event = eventRepository.getOne(eventId);
        eventRepository.save(fillEntity(event, participantId));
    }

    public void removeParticipant(Long eventId, Long participantId) {
        Event event = eventRepository.getOne(eventId);
        eventRepository.save(fillEntity(event, participantId));
    }

    public Event getEvent(Long id) {
        return eventRepository.getOne(id);
    }

    @Transactional
    public void removeEvent(Event event) {
        eventRepository.deleteById(event.getId());
    }

    private Event fillEntity(Event event, Long participantId) {
        Set<Long> userIds = event.getParticipantIds();
        userIds.add(participantId);
        event.setParticipantIds(userIds);
        Set<User> users = event.getParticipants();
        users.add(userRepository.getOne(participantId));
        event.setParticipants(users);
        return event;
    }

    @Transactional
    public void deleteEvent(Long eventID, Long ownerId) {
        Event event = eventRepository.getOne(eventID);
        if (!event.getId().equals(ownerId))
            throw new AccessDeniedException(ResponseCode.ACCESS_DENIED.name());
        else
            eventRepository.deleteById(eventID);
    }
}
