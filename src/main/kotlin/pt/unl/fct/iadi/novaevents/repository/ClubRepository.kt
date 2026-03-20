package pt.unl.fct.iadi.novaevents.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import pt.unl.fct.iadi.novaevents.controller.dto.ClubResponse
import pt.unl.fct.iadi.novaevents.model.Club

interface ClubRepository : JpaRepository<Club, Long> {

    @Query("SELECT new pt.unl.fct.iadi.novaevents.controller.dto.ClubResponse(\n" +
            "            c.id, c.name, c.description, c.category, COUNT(e)\n" +
            "        )\n" +
            "        FROM Club c LEFT JOIN Event e ON c.id = e.clubId\n" +
            "        GROUP BY c.id, c.name, c.description, c.category")
    fun findAllWithEventCount(): List<ClubResponse>
}