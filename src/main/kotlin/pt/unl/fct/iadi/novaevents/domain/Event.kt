package pt.unl.fct.iadi.novaevents.domain

import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull
import java.time.LocalDate

data class Event(
    val id: Long,
    val clubId: Long,
    val name: String,
    val date: LocalDate,
    val location: String?,
    val type: EventType,
    val description: String?
) {
}