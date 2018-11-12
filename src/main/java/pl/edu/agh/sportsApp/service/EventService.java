package pl.edu.agh.sportsApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.sportsApp.dto.EventDTO;
import pl.edu.agh.sportsApp.dto.EventRequestDTO;
import pl.edu.agh.sportsApp.dto.ResponseCode;
import pl.edu.agh.sportsApp.exceptionHandler.exceptions.NoPermissionsException;
import pl.edu.agh.sportsApp.exceptionHandler.exceptions.ValidationException;
import pl.edu.agh.sportsApp.model.Event;
import pl.edu.agh.sportsApp.model.chat.EventChat;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.model.photo.EventPhoto;
import pl.edu.agh.sportsApp.repository.event.EventRepository;
import pl.edu.agh.sportsApp.repository.event.projection.RatingFormElement;
import pl.edu.agh.sportsApp.repository.user.UserRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static pl.edu.agh.sportsApp.dto.ResponseCode.METHOD_ARGS_NOT_VALID;

@Service
public class EventService {
    private EventRepository eventRepository;
    private UserRepository userRepository;
    private ChatStorage chatStorage;

    @Autowired
    public EventService(EventRepository eventRepository, UserRepository userRepository, ChatStorage chatStorage) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.chatStorage = chatStorage;
    }

    public void createEvent(EventRequestDTO eventRequestDTO, User owner) {
        LocalDateTime currentDate = LocalDateTime.now();
        if (eventRequestDTO.getStartDate().isBefore(currentDate) || eventRequestDTO.getStartDate().isAfter(currentDate.plusMonths(1)))
            throw new ValidationException(METHOD_ARGS_NOT_VALID.name());
        EventChat eventChat = chatStorage.createEventChat();
        Event newEvent = eventRequestDTO.parseEvent();
        newEvent.setOwnerId(owner.getId());
        newEvent.setOwner(owner);
        Map<Long, User> participants = new HashMap<>();
        participants.put(owner.getId(), owner);
        newEvent.setParticipants(participants);
        newEvent.setEventChat(eventChat);
        eventRepository.save(newEvent);
    }

    public void addParticipant(Long eventId, Long participantId) {
        Event event = eventRepository.getOne(eventId);
        if (event.getOwner().getId().equals(participantId))
            throw new NoPermissionsException(ResponseCode.PERMISSION_DENIED.name());
        eventRepository.save(addUserToEvent(event, participantId));
    }

    public void removeParticipant(Long eventId, Long participantId) {
        Event event = eventRepository.getOne(eventId);
        if (event.getOwner().getId().equals(participantId))
            throw new NoPermissionsException(ResponseCode.PERMISSION_DENIED.name());
        eventRepository.save(removeUserFromEvent(event, participantId));
    }

    public Event getEvent(Long id) {
        return eventRepository.getOne(id);
    }

    @Transactional
    public void removeEvent(Event event) {
        eventRepository.deleteById(event.getId());
    }

    private Event addUserToEvent(Event event, Long participantId) {
        Map<Long, User> users = event.getParticipants();
        User user = userRepository.getOne(participantId);
        users.put(user.getId(), user);
        event.setParticipants(users);
        return event;
    }

    private Event removeUserFromEvent(Event event, Long participantId) {
        Map<Long, User> users = event.getParticipants();
        User user = userRepository.getOne(participantId);
        users.remove(user.getId());
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

    public Optional<Event> findEventById(Long id) {
        return eventRepository.findById(id);
    }

    public void addEventPhoto(Event event, EventPhoto eventPhoto) {
        event.addEventPhoto(eventPhoto);
        eventRepository.save(event);
    }

    public void removeEventPhoto(Event event, EventPhoto eventPhoto) {
        event.removeEventPhoto(eventPhoto);
        eventRepository.save(event);
    }

    public List<EventDTO> getAllEvents() {
        LocalDateTime startDate = LocalDateTime.now().minusMinutes(30);
        LocalDateTime endDate = startDate.plusMonths(1);
        return eventRepository.getALlByStartDateIsBetween(startDate, endDate).stream()
                .map(Event::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<RatingFormElement> getUserRatingFormForEvent(Long eventId, Long userId) {
        return eventRepository.getUserRatingFormForEvent(eventId, userId);
    }
}
