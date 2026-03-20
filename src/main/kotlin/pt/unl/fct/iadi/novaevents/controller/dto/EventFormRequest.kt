package pt.unl.fct.iadi.novaevents.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.format.annotation.DateTimeFormat
import pt.unl.fct.iadi.novaevents.model.Event
import pt.unl.fct.iadi.novaevents.model.EventType
import java.time.LocalDate

data class EventFormRequest(
    @field:NotBlank(message = "Name is required")
    val name: String? = null,
    @field:NotNull(message = "Date is required")
    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    val date: LocalDate? = null,
    @field:NotNull(message = "Event type is required")
    val type: EventType? = null,
    val location: String? = null,
    val description: String? = null
) {

    fun toEvent(): Event {
        return Event().apply {
            this.name = this@EventFormRequest.name
            this.date = this@EventFormRequest.date
            this.type = this@EventFormRequest.type
            this.location = this@EventFormRequest.location
            this.description = this@EventFormRequest.description
        }
    }
}