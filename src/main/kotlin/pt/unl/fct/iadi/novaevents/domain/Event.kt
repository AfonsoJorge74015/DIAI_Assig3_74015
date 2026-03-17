package pt.unl.fct.iadi.novaevents.domain

import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull
import java.time.LocalDate

data class Event(
    val id: Long,
    val clubId: Long,
    @NotBlank(message = "Name is required")
    val name: String,
    @NotNull("Date is required")
    val date: LocalDate,
    val location: String,
    @NotNull("Event type is required")
    val type: EventType,
    val description: String
) {
}