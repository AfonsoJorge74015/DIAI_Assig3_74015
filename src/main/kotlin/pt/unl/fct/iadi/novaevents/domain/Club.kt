package pt.unl.fct.iadi.novaevents.domain

data class Club(
    val id: Long,
    val name: String,
    val description: String,
    val category: ClubCategory
) {
}