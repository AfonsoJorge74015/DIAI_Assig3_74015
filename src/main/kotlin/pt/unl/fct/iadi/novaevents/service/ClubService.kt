package pt.unl.fct.iadi.novaevents.service

import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.controller.dto.ClubResponse
import pt.unl.fct.iadi.novaevents.model.Club
import pt.unl.fct.iadi.novaevents.repository.ClubRepository

@Service
class ClubService(
    private val repository: ClubRepository
) {

    fun getAllClubsWithEventCount(): List<ClubResponse> {
        return repository.findAllWithEventCount()
    }

    fun getAllClubs(): List<Club> {
        return repository.findAll()
    }

    fun getClub(clubId: Long): Club {
        return repository.findById(clubId)
            .orElseThrow{ NoSuchElementException("No club found with id $clubId") }
    }
}