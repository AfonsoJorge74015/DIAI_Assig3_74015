package pt.unl.fct.iadi.novaevents.utils

import org.springframework.stereotype.Component
import pt.unl.fct.iadi.novaevents.controller.dto.ClubDTO
import pt.unl.fct.iadi.novaevents.controller.dto.EventDTO
import pt.unl.fct.iadi.novaevents.domain.Club
import pt.unl.fct.iadi.novaevents.domain.Event

@Component
class Mappers {

    fun clubToDTO(club: Club): ClubDTO {
        return ClubDTO(
            id = club.id,
            name = club.name,
            description = club.description,
            category = club.category)
    }

    fun dtoToClub(dto: ClubDTO): Club {
        return Club(
            dto.id,
            dto.name,
            dto.description,
            dto.category)
    }


    fun eventToDTO(event: Event): EventDTO {
        return EventDTO(
            id = event.id,
            clubId = event.clubId,
            name = event.name,
            date = event.date,
            location = event.location,
            type = event.type,
            description = event.description)
    }

    fun dtoToEvent(dto: EventDTO): Event {
        return Event(
            id = dto.id,
            clubId = dto.clubId,
            name = dto.name,
            date = dto.date,
            location = dto.location,
            type = dto.type,
            description = dto.description)
    }
}