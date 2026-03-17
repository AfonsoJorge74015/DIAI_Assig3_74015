package pt.unl.fct.iadi.novaevents.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import pt.unl.fct.iadi.novaevents.domain.EventType
import pt.unl.fct.iadi.novaevents.service.NovaService

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
        model["events"] = service.getFilteredEvent(type, clubId)
        model["clubs"] = service.getAllClubs()
        return "events/list"
    }

    override fun eventDetails(clubId: Long, eventId: Long, model: ModelMap): String {
        model["club"] = service.getClub(clubId)
        model["event"] = service.getEvent(eventId)
        return "events/detail"
    }
}