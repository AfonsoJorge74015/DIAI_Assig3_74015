package pt.unl.fct.iadi.novaevents.controller

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import pt.unl.fct.iadi.novaevents.model.EventType
import pt.unl.fct.iadi.novaevents.repository.EventTypeRepository

@Component
class EventTypeConverter(
    private val repository: EventTypeRepository
) : Converter<String, EventType> {

    override fun convert(source: String): EventType? {
        if (source.isBlank()) return null
        val id = source.toLongOrNull() ?: return null
        return repository.findById(id).orElse(null)
    }
}