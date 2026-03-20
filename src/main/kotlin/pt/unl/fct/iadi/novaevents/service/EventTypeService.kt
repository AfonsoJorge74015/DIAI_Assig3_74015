package pt.unl.fct.iadi.novaevents.service

import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.model.EventType
import pt.unl.fct.iadi.novaevents.repository.EventTypeRepository

@Service
class EventTypeService(
    private val repository: EventTypeRepository
) {

    fun allEventTypes(): List<EventType> {
        return repository.findAll()
    }
}