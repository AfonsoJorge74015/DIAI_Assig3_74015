package pt.unl.fct.iadi.novaevents.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.controller.dto.EventFormRequest
import pt.unl.fct.iadi.novaevents.model.Event
import pt.unl.fct.iadi.novaevents.model.EventType
import pt.unl.fct.iadi.novaevents.repository.EventRepository

@Service
class EventService(
    private val repository: EventRepository
) {

    fun getFilteredEvents(type: EventType?, clubId: Long?): List<Event> {
        return repository.findByTypeAndClubId(type, clubId)
    }

    fun getEvent(eventId: Long): Event {
        return repository.findById(eventId)
            .orElseThrow { NoSuchElementException("No event found with id $eventId") }
    }

    fun getEventByName(eventName: String): Boolean {
        return repository.existsByName(eventName)
    }

    fun getEventByNameExcludingId(eventName: String, eventId: Long): Boolean {
        return repository.existsByNameAndIdNot(eventName, eventId)
    }

    @Transactional
    fun createEvent(clubId: Long, form: EventFormRequest): Event {
        val event = Event().apply {
            this.clubId = clubId
            this.name = form.name
            this.date = form.date
            this.location = form.location
            this.type = form.type
            this.description = form.description
        }

        return repository.save(event)
    }

    @Transactional
    fun updateEvent(eventId: Long, clubId: Long, form: EventFormRequest) {
        val event = repository.findById(eventId)
            .orElseThrow { NoSuchElementException("No event found with id $eventId") }

        event.clubId = clubId
        event.name = form.name
        event.date = form.date
        event.location = form.location
        event.type = form.type
        event.description = form.description

        repository.save(event)
    }

    fun deleteEvent(eventId: Long) {
        repository.deleteById(eventId)
    }
}