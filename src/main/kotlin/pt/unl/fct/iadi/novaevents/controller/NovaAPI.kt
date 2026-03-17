package pt.unl.fct.iadi.novaevents.controller

import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import pt.unl.fct.iadi.novaevents.domain.EventType


interface NovaAPI {

    @GetMapping("/")
    fun home(): String

    @GetMapping("/clubs")
    fun listClubs(model: ModelMap): String

    @GetMapping("clubs/{clubId}")
    fun clubDetails(@PathVariable clubId: Long, model: ModelMap): String

    @GetMapping("/events")
    fun listEvents(@RequestParam(required = false) type: EventType?,
                   @RequestParam(required = false) clubId: Long?,
                   model: ModelMap): String

    @GetMapping("/clubs/{clubId}/events/{eventId}")
    fun eventDetails(@PathVariable clubId: Long, @PathVariable eventId: Long, model: ModelMap): String
}