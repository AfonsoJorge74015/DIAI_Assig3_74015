package pt.unl.fct.iadi.novaevents.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class EventFormRequest(
    @field:NotBlank(message = "Name is required")
    val name: String? = null,
    @field:NotNull(message = "Date is required")
    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    val date: LocalDate? = null,
    @field:NotBlank(message = "Event type is required")
    val type: String? = null,
    val location: String? = null,
    val description: String? = null
) {
}