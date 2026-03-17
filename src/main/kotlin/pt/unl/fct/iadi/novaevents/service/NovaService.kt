package pt.unl.fct.iadi.novaevents.service

import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.controller.dto.ClubDTO
import pt.unl.fct.iadi.novaevents.controller.dto.EventDTO
import pt.unl.fct.iadi.novaevents.domain.Club
import pt.unl.fct.iadi.novaevents.domain.ClubCategory
import pt.unl.fct.iadi.novaevents.domain.Event
import pt.unl.fct.iadi.novaevents.domain.EventType
import pt.unl.fct.iadi.novaevents.utils.Mappers
import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap

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

    //val events: ConcurrentHashMap<Club, MutableList<Event>> = ConcurrentHashMap()

    private val events = mutableListOf(
        Event(
            id = 1,
            clubId = 1, // Chess Club
            name = "Beginner's Chess Workshop",
            date = LocalDate.of(2026, 3, 20),
            location = "Room A101",
            type = EventType.WORKSHOP,
            description = "Learn the basics of chess, including piece movements, board setup, and simple opening strategies."
        ),
        Event(
            id = 2,
            clubId = 2, // Robotics Club
            name = "RoboCup Preparation Meeting",
            date = LocalDate.of(2026, 3, 28),
            location = "Engineering Lab 1",
            type = EventType.MEETING,
            description = "Initial planning and team forming for the upcoming national RoboCup tournament. All members welcome!"
        ),
        Event(
            id = 3,
            clubId = 5, // Film Society
            name = "Kubrick Retrospective Screening",
            date = LocalDate.of(2026, 4, 10),
            location = "Cinema Room",
            type = EventType.SOCIAL,
            description = "Join us for a cozy evening watching a classic Stanley Kubrick film. Popcorn and drinks provided."
        )
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

    fun getFilteredEvent(type: EventType?, clubId: Long?): List<EventDTO> {
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
}