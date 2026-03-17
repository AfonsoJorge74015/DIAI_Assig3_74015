package pt.unl.fct.iadi.novaevents.controller

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute
import pt.unl.fct.iadi.novaevents.domain.EventType

@ControllerAdvice
class GlobalControllerAdvice {

    @ModelAttribute("eventTypes")
    fun eventTypes() = EventType.entries.toTypedArray()
}