package pt.unl.fct.iadi.novaevents.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import pt.unl.fct.iadi.novaevents.controller.dto.EventFormRequest
import pt.unl.fct.iadi.novaevents.service.ClubService
import pt.unl.fct.iadi.novaevents.service.EventService
import pt.unl.fct.iadi.novaevents.service.EventTypeService
import pt.unl.fct.iadi.novaevents.service.WeatherService
import pt.unl.fct.iadi.novaevents.utils.Mappers
import kotlin.collections.set

@Controller
class NovaController(
    private val clubService: ClubService,
    private val eventService: EventService,
    private val eventTypeService: EventTypeService,
    private val weatherService: WeatherService,
    private val mappers: Mappers
) : NovaAPI {

    override fun home(): String {
        return "redirect:/clubs"
    }

    override fun listClubs(model: ModelMap): String {
        val clubs = clubService.getAllClubsWithEventCount()
        model["clubs"] = clubs
        return "clubs/list"
    }

    override fun clubDetails(clubId: Long, model: ModelMap): String {
        val club = clubService.getClub(clubId)
        model["club"] = mappers.toClubResponse(club)
        return "clubs/detail"
    }

    override fun listEvents(type: String?, clubId: Long?, model: ModelMap): String {
        val events = eventService.getFilteredEvents(type, clubId)
        val clubs = clubService.getAllClubs()
        model["events"] = events.map { mappers.toEventResponse(it) }
        model["clubs"] = clubs.map { mappers.toClubResponse(it) }
        model["eventTypes"] = eventTypeService.allEventTypes()
        return "events/list"
    }

    override fun eventDetails(clubId: Long, eventId: Long, model: ModelMap): String {
        val club = clubService.getClub(clubId)
        val event = eventService.getEvent(eventId)
        model["club"] = mappers.toClubResponse(club)
        model["event"] = mappers.toEventResponse(event)
        return "events/detail"
    }

    override fun showForm(clubId: Long, model: ModelMap): String {
        val club = clubService.getClub(clubId)
        model["club"] = mappers.toClubResponse(club)
        model["eventForm"] = EventFormRequest()
        model["eventTypes"] = eventTypeService.allEventTypes()
        return "events/form"
    }

    override fun submitFormNew(clubId: Long, event: EventFormRequest, bindingResult: BindingResult, model: ModelMap): String {
        val club = clubService.getClub(clubId)

        if (club.name?.contains("Hiking", ignoreCase = true) == true) {
            val location = event.location?.trim()
            if (location.isNullOrEmpty()) {
                bindingResult.rejectValue("location", "required", "Location is required for outdoor events")
            } else {
                val isRaining = weatherService.isRaining(location)
                if (isRaining == true) {
                    bindingResult.rejectValue("location", "weather",
                        "It is currently raining at \"${event.location}\" — outdoor events cannot be created in bad weather")
                }
            }
        }

        if (event.name != null && eventService.getEventByName(event.name)) {
            bindingResult.rejectValue("name", "duplicate", "An event with this name already exists")
        }

        if(bindingResult.hasErrors()) {
            model["club"] = mappers.toClubResponse(club)
            model["eventTypes"] = eventTypeService.allEventTypes()
            return "events/form"
        }

        val newEvent = eventService.createEvent(clubId, event)
        return "redirect:/clubs/${clubId}/events/${newEvent.id}" // Returns 302 Redirect
    }

    @PreAuthorize("hasRole('ADMIN') or @eventRepository.findById(#eventId).orElse(null)?.owner?.username == authentication.name")
    override fun editForm(clubId: Long, eventId: Long, model: ModelMap): String {
        val currentEvent = eventService.getEvent(eventId)
        val eventDto = mappers.toEventResponse(currentEvent)
        val club = clubService.getClub(clubId)

        model["club"] = mappers.toClubResponse(club)
        model["event"] = eventDto
        model["eventTypes"] = eventTypeService.allEventTypes()
        model["eventForm"] = EventFormRequest(
            name = eventDto.name,
            date = eventDto.date,
            type = eventDto.type,
            location = eventDto.location,
            description = eventDto.description
        )
        return "events/editForm"
    }

    @PreAuthorize("@eventRepository.findById(#eventId).orElse(null)?.owner?.username == authentication.name")
    override fun submitFormEdit(clubId: Long, eventId: Long, event: EventFormRequest, bindingResult: BindingResult, model: ModelMap): String {
        val club = clubService.getClub(clubId)

        if (club.name?.contains("Hiking", ignoreCase = true) == true) {
            val location = event.location?.trim()
            if (location.isNullOrEmpty()) {
                bindingResult.rejectValue("location", "required", "Location is required for outdoor events")
            } else {
                val isRaining = weatherService.isRaining(location)
                if (isRaining == true) {
                    bindingResult.rejectValue("location", "weather",
                        "It is currently raining at \"${event.location}\" — outdoor events cannot be created in bad weather")
                }
            }
        }

        if (eventService.getEventByNameExcludingId(event.name!!, eventId)) {
            bindingResult.rejectValue("name", "duplicate", "An event with this name already exists")
        }

        if(bindingResult.hasErrors()) {
            model["club"] = mappers.toClubResponse(club)
            model["event"] = mappers.toEventResponse(eventService.getEvent(eventId))
            model["eventTypes"] = eventTypeService.allEventTypes()
            return "events/editForm" // Returns 200 OK
        }

        eventService.updateEvent(eventId, clubId, event)
        return "redirect:/clubs/${clubId}/events/${eventId}" // Returns 302 Redirect
    }

    @PreAuthorize("hasRole('ADMIN') or @eventRepository.findById(#eventId).orElse(null)?.owner?.username == authentication.name")
    override fun deleteConfirm(clubId: Long, eventId: Long, model: ModelMap): String {
        val club = clubService.getClub(clubId)
        val event = eventService.getEvent(eventId)
        model["club"] = mappers.toClubResponse(club)
        model["event"] = mappers.toEventResponse(event)
        return "events/delete"
    }

    @PreAuthorize("hasRole('ADMIN') or @eventRepository.findById(#eventId).orElse(null)?.owner?.username == authentication.name")
    override fun deleteEvent(clubId: Long, eventId: Long, model: ModelMap): String {
        eventService.deleteEvent(eventId)
        return "redirect:/clubs/${clubId}"
    }
}