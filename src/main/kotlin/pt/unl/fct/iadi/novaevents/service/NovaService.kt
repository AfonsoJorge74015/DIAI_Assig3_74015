package pt.unl.fct.iadi.novaevents.service

import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.controller.dto.ClubDTO
import pt.unl.fct.iadi.novaevents.controller.dto.EventDTO
import pt.unl.fct.iadi.novaevents.domain.Club
import pt.unl.fct.iadi.novaevents.domain.ClubCategory
import pt.unl.fct.iadi.novaevents.domain.Event
import pt.unl.fct.iadi.novaevents.domain.EventType
import pt.unl.fct.iadi.novaevents.controller.dto.EventForm
import pt.unl.fct.iadi.novaevents.utils.Mappers
import java.util.Random

@Service
class NovaService(
    private val mappers: Mappers
) {

    val clubs: List<Club> =
        listOf(
            Club(1, "Chess Club", "", ClubCategory.ACADEMIC),
            Club(2, "Robotics Club", "The Robotics Club is the place to turn ideas into machines", ClubCategory.TECHNOLOGY),
            Club(3, "Photography Club", "", ClubCategory.ARTS),
            Club(4, "Hiking & Outdoors Club", "", ClubCategory.SPORTS),
            Club(5, "Film Society", "", ClubCategory.CULTURAL)
        )

    private val events = mutableListOf<Event>(
        Event(1, 1, "Beginner's Chess Workshop", java.time.LocalDate.of(2026, 3, 10),
            "Room A101", EventType.WORKSHOP, "Learn chess basics."),
        Event(2, 1, "Spring Chess Tournament", java.time.LocalDate.of(2026, 5, 15),
            "Main Hall", EventType.COMPETITION, "Annual tournament.")
    )

    fun getAllClubs(): List<ClubDTO> {
        return clubs.map { mappers.clubToDTO(it)}
    }

    fun getClub(clubId: Long): ClubDTO {
        val club = clubs.find { it.id == clubId }
        if(club == null) {
            throw NoSuchElementException("No club found with id $clubId")
        }
        return mappers.clubToDTO(club)
    }

    fun getFilteredEvents(type: EventType?, clubId: Long?): List<EventDTO> {
        return events.map { mappers.eventToDTO(it) }
            .filter { event ->
                val matchType = (type == null) || (event.type == type)
                val matchClub = (clubId == null) || (event.clubId == clubId)
                matchType && matchClub
            }
    }

    fun getEvent(eventId: Long): EventDTO {
        val event = events.find { it.id == eventId }
        if(event == null) {
            throw NoSuchElementException("No event found with id $eventId")
        }
        return mappers.eventToDTO(event)
    }

    fun createEvent(clubId: Long, form: EventForm): EventDTO {
        val event = Event(
            id = Random().nextLong(),
            clubId = clubId,
            name = form.name!!,
            date = form.date!!,
            location = form.location,
            type = form.type!!,
            description = form.description
        )
        events.add(event)
        return mappers.eventToDTO(event)
    }

    fun updateEvent(eventId: Long, clubId: Long, form: EventForm): EventDTO {
        val idx = events.indexOfFirst { it.id == eventId }
        val updated = Event(
                eventId,
                clubId,
                name = form.name!!,
                date = form.date!!,
                location = form.location,
                type = form.type!!,
                description = form.description
            )
        events[idx] = updated
        return mappers.eventToDTO(updated)
    }

    fun deleteEvent(eventId: Long) {
        events.removeIf { it.id == eventId }
    }
}