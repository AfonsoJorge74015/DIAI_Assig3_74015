package pt.unl.fct.iadi.novaevents.controller.dto

import pt.unl.fct.iadi.novaevents.domain.ClubCategory

data class ClubDTO(
    val id: Long,
    val name: String,
    val description: String,
    val category: ClubCategory
) {
}