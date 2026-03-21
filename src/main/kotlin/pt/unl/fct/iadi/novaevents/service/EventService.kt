package pt.unl.fct.iadi.novaevents.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.controller.dto.EventFormRequest
import pt.unl.fct.iadi.novaevents.model.Event
import pt.unl.fct.iadi.novaevents.model.EventType
import pt.unl.fct.iadi.novaevents.repository.EventRepository
import pt.unl.fct.iadi.novaevents.repository.EventTypeRepository

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val eventTypeRepository: EventTypeRepository
) {

    fun getFilteredEvents(type: EventType?, clubId: Long?): List<Event> {
        return eventRepository.findByTypeAndClubId(type, clubId)
    }

    fun getEvent(eventId: Long): Event {
        return eventRepository.findById(eventId)
            .orElseThrow { NoSuchElementException("No event found with id $eventId") }
    }

    fun getEventByName(eventName: String): Boolean {
        return eventRepository.existsByName(eventName)
    }

    fun getEventByNameExcludingId(eventName: String, eventId: Long): Boolean {
        return eventRepository.existsByNameAndIdNot(eventName, eventId)
    }

    @Transactional
    fun createEvent(clubId: Long, form: EventFormRequest): Event {
        val event = Event().apply {
            this.clubId = clubId
            this.name = form.name
            this.date = form.date
            this.location = form.location
            this.type = eventTypeRepository.findByName(form.type!!)
            this.description = form.description
        }

        return eventRepository.save(event)
    }

    @Transactional
    fun updateEvent(eventId: Long, clubId: Long, form: EventFormRequest) {
        val event = eventRepository.findById(eventId)
            .orElseThrow { NoSuchElementException("No event found with id $eventId") }

        event.clubId = clubId
        event.name = form.name
        event.date = form.date
        event.location = form.location
        event.type = eventTypeRepository.findByName(form.type!!)
        event.description = form.description

        eventRepository.save(event)
    }

    fun deleteEvent(eventId: Long) {
        eventRepository.deleteById(eventId)
    }
}