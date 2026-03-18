package pt.unl.fct.iadi.novaevents.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ExceptionHandler
import pt.unl.fct.iadi.novaevents.domain.EventType
import pt.unl.fct.iadi.novaevents.service.NovaService
import pt.unl.fct.iadi.novaevents.controller.dto.EventForm
import kotlin.collections.set

@Controller
class NovaController(
    private val service: NovaService) : NovaAPI {

    override fun home(): String {
        return "redirect:/clubs"
    }

    override fun listClubs(model: ModelMap): String {
        model["clubs"] = service.getAllClubs()
        return "clubs/list"
    }

    override fun clubDetails(clubId: Long, model: ModelMap): String {
        model["club"] = service.getClub(clubId)
        return "clubs/detail"
    }

    override fun listEvents(type: EventType?, clubId: Long?, model: ModelMap): String {
        model["events"] = service.getFilteredEvents(type, clubId)
        model["clubs"] = service.getAllClubs()
        return "events/list"
    }

    override fun eventDetails(clubId: Long, eventId: Long, model: ModelMap): String {
        model["club"] = service.getClub(clubId)
        model["event"] = service.getEvent(eventId)
        return "events/detail"
    }

    override fun showForm(clubId: Long, model: ModelMap): String {
        model["club"] = service.getClub(clubId)
        model["eventForm"] = EventForm()
        return "events/form"
    }

    override fun submitFormNew(clubId: Long, event: EventForm, bindingResult: BindingResult, model: ModelMap): String {
        if(bindingResult.hasErrors()) {
            model["club"] = service.getClub(clubId)
            return "events/form"
        }
        val newEvent = service.createEvent(clubId, event)
        return "redirect:/clubs/${clubId}/events/${newEvent.id}"
    }

    override fun editForm(clubId: Long, eventId: Long, model: ModelMap): String {
        val currentEvent = service.getEvent(eventId)
        model["club"] = service.getClub(clubId)
        model["event"] = currentEvent
        model["eventForm"] = EventForm(
            name = currentEvent.name,
            date = currentEvent.date,
            type = currentEvent.type,
            location = currentEvent.location,
            description = currentEvent.description
        )
        return "events/editForm"
    }

    override fun submitFormEdit(
        clubId: Long,
        eventId: Long,
        event: EventForm,
        bindingResult: BindingResult,
        model: ModelMap
    ): String {
        if(bindingResult.hasErrors()) {
            model["club"] = service.getClub(clubId)
            model["event"] = service.getEvent(eventId)
            return "events/editForm"
        }
        val updatedEvent = service.updateEvent(eventId, clubId, event)
        return "redirect:/clubs/${clubId}/events/${updatedEvent.id}"
    }

    override fun deleteConfirm(clubId: Long, eventId: Long, model: ModelMap): String {
        model["club"] = service.getClub(clubId)
        model["event"] = service.getEvent(eventId)
        return "events/delete"
    }

    override fun deleteEvent(clubId: Long, eventId: Long, model: ModelMap): String {
        service.deleteEvent(eventId)
        return "redirect:/clubs"
    }
}