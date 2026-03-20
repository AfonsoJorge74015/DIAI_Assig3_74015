package pt.unl.fct.iadi.novaevents.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pt.unl.fct.iadi.novaevents.model.Event
import pt.unl.fct.iadi.novaevents.model.EventType

@Repository
interface EventRepository : JpaRepository<Event, Long> {
    fun existsByName(name: String) : Boolean;
    fun existsByNameAndIdNot(name: String, id: Long) : Boolean;
    fun findByTypeAndClubId(type: EventType?, clubId: Long?): List<Event>
}