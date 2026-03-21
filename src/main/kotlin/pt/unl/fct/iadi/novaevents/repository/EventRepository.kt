package pt.unl.fct.iadi.novaevents.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import pt.unl.fct.iadi.novaevents.model.Event
import pt.unl.fct.iadi.novaevents.model.EventType

@Repository
interface EventRepository : JpaRepository<Event, Long> {
    fun existsByName(name: String) : Boolean;
    fun existsByNameAndIdNot(name: String, id: Long) : Boolean;
    @Query("""
        SELECT e FROM Event e 
        LEFT JOIN FETCH e.type
        WHERE (:type IS NULL OR e.type = :type) 
        AND (:clubId IS NULL OR e.clubId = :clubId)
    """)
    fun findByTypeAndClubId(type: EventType?, clubId: Long?): List<Event>
}