package pt.unl.fct.iadi.novaevents.controller.dto

import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull
import pt.unl.fct.iadi.novaevents.domain.EventType
import java.time.LocalDate

data class EventDTO(
    val id: Long,
    val clubId: Long,
    @field:NotBlank(message = "Name is required")
    val name: String,
    @field:NotNull("Date is required")
    val date: LocalDate,
    val location: String?,
    @field:NotNull("Event type is required")
    val type: EventType,
    val description: String?
) {
}