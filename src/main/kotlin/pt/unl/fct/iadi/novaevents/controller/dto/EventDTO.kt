package pt.unl.fct.iadi.novaevents.controller.dto

import pt.unl.fct.iadi.novaevents.domain.EventType
import java.time.LocalDate

data class EventDTO(
    val id: Long,
    val clubId: Long,
    val name: String,
    val date: LocalDate,
    val location: String,
    val type: EventType,
    val description: String
) {
}