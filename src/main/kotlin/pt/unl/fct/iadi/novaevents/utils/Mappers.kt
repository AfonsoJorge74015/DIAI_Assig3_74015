package pt.unl.fct.iadi.novaevents.utils

import org.springframework.stereotype.Component
import pt.unl.fct.iadi.novaevents.controller.dto.ClubResponse
import pt.unl.fct.iadi.novaevents.controller.dto.EventResponse
import pt.unl.fct.iadi.novaevents.model.Club
import pt.unl.fct.iadi.novaevents.model.Event

@Component
class Mappers {

    fun toEventResponse(event: Event): EventResponse {
        return EventResponse(
            id = event.id!!,
            clubId = event.clubId!!,
            name = event.name!!,
            date = event.date!!,
            location = event.location,
            type = event.type!!.name!!,
            description = event.description
        )
    }

    fun toClubResponse(club: Club): ClubResponse {
        return ClubResponse(
            id = club.id!!,
            name = club.name!!,
            description = club.description!!,
            category = club.category!!
        )
    }
}