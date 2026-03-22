package pt.unl.fct.iadi.novaevents.controller

import jakarta.validation.Valid
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam
import pt.unl.fct.iadi.novaevents.controller.dto.EventFormRequest


interface NovaAPI {

    @GetMapping("/")
    fun home(): String

    @GetMapping("/clubs")
    fun listClubs(model: ModelMap): String

    @GetMapping("clubs/{clubId}")
    fun clubDetails(@PathVariable clubId: Long, model: ModelMap): String

    @GetMapping("/events")
    fun listEvents(@RequestParam(required = false) type: String?,
                   @RequestParam(required = false) clubId: Long?,
                   model: ModelMap): String

    @GetMapping("/clubs/{clubId}/events/{eventId}")
    fun eventDetails(@PathVariable clubId: Long, @PathVariable eventId: Long, model: ModelMap): String

    @GetMapping("/clubs/{clubId}/events/new")
    fun showForm(@PathVariable clubId: Long, model: ModelMap): String

    @PostMapping("/clubs/{clubId}/events")
    fun submitFormNew(
        @PathVariable clubId: Long,
        @Valid @ModelAttribute("eventForm") event: EventFormRequest,
        bindingResult: BindingResult, model: ModelMap
    ): String

    @GetMapping("/clubs/{clubId}/events/{eventId}/edit")
    fun editForm(@PathVariable clubId: Long, @PathVariable eventId: Long, model: ModelMap): String

    @PutMapping("/clubs/{clubId}/events/{eventId}")
    fun submitFormEdit(
        @PathVariable clubId: Long,
        @PathVariable eventId: Long,
        @Valid @ModelAttribute("eventForm") event: EventFormRequest,
        bindingResult: BindingResult, model: ModelMap
    ): String

    @GetMapping("/clubs/{clubId}/events/{eventId}/delete")
    fun deleteConfirm(@PathVariable clubId: Long,
                      @PathVariable eventId: Long,
                      model: ModelMap): String

    @DeleteMapping("/clubs/{clubId}/events/{eventId}")
    fun deleteEvent(@PathVariable clubId: Long, @PathVariable eventId: Long, model: ModelMap): String
}