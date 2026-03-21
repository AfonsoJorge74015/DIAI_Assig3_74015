package pt.unl.fct.iadi.novaevents.controller.dto

import pt.unl.fct.iadi.novaevents.model.ClubCategory

data class ClubResponse(
    val id: Long,
    val name: String,
    val description: String,
    val category: ClubCategory,
    val events: Long = 0L
) {
}