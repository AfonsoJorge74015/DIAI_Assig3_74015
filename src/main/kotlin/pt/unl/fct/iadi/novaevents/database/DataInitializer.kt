package pt.unl.fct.iadi.novaevents.database

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import pt.unl.fct.iadi.novaevents.model.Club
import pt.unl.fct.iadi.novaevents.model.ClubCategory
import pt.unl.fct.iadi.novaevents.model.Event
import pt.unl.fct.iadi.novaevents.model.EventType
import pt.unl.fct.iadi.novaevents.repository.ClubRepository
import pt.unl.fct.iadi.novaevents.repository.EventRepository
import pt.unl.fct.iadi.novaevents.repository.EventTypeRepository
import java.time.LocalDate

@Component
class DataInitializer(
    private val eventTypeRepository: EventTypeRepository,
    private val clubRepository: ClubRepository,
    private val eventRepository: EventRepository
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        if (eventTypeRepository.count() > 0 || clubRepository.count() > 0) {
            println("Database already seeded. Skipping initialization.")
            return
        }

        println("Seeding database...")

        val typeWorkshop = eventTypeRepository.save(EventType().apply { name = "WORKSHOP" })
        val typeTalk = eventTypeRepository.save(EventType().apply { name = "TALK" })
        val typeCompetition = eventTypeRepository.save(EventType().apply { name = "COMPETITION" })
        val typeSocial = eventTypeRepository.save(EventType().apply { name = "SOCIAL" })
        val typeMeeting = eventTypeRepository.save(EventType().apply { name = "MEETING" })
        val typeOther = eventTypeRepository.save(EventType().apply { name = "OTHER" })

        val chessClub = clubRepository.save(Club().apply {
            name = "Chess Club"
            description = "For those who love strategy and board games."
            category = ClubCategory.ACADEMIC
        })
        val roboticsClub = clubRepository.save(Club().apply {
            name = "Robotics Club"
            description = "The Robotics Club is the place to turn ideas into machines"
            category = ClubCategory.TECHNOLOGY
        })
        val photoClub = clubRepository.save(Club().apply {
            name = "Photography Club"
            description = "Capture the world through your lens."
            category = ClubCategory.ARTS
        })
        val hikingClub = clubRepository.save(Club().apply {
            name = "Hiking & Outdoors Club"
            description = "Explore nature and local trails."
            category = ClubCategory.SPORTS
        })
        val filmSociety = clubRepository.save(Club().apply {
            name = "Film Society"
            description = "Watch, critique, and make movies."
            category = ClubCategory.CULTURAL
        })

        val initialEvents = listOf(
            // Chess Club Events
            Event().apply { clubId = chessClub.id; name = "Beginner's Chess Workshop"; date = LocalDate.of(2026, 3, 10); location = "Room A101"; type = typeWorkshop; description = "Learn chess basics." },
            Event().apply { clubId = chessClub.id; name = "Spring Chess Tournament"; date = LocalDate.of(2026, 5, 15); location = "Main Hall"; type = typeCompetition; description = "Annual tournament." },
            Event().apply { clubId = chessClub.id; name = "Grandmaster Talk"; date = LocalDate.of(2026, 4, 5); location = "Auditorium"; type = typeTalk; description = "Q&A with a local master." },

            // Robotics Club Events
            Event().apply { clubId = roboticsClub.id; name = "Drone Building 101"; date = LocalDate.of(2026, 3, 20); location = "Lab 3"; type = typeWorkshop; description = "Build a drone from scratch." },
            Event().apply { clubId = roboticsClub.id; name = "Robot Wars Exhibition"; date = LocalDate.of(2026, 6, 10); location = "Gymnasium"; type = typeCompetition; description = "Battle of the bots!" },
            Event().apply { clubId = roboticsClub.id; name = "Weekly Build Meeting"; date = LocalDate.of(2026, 3, 12); location = "Lab 3"; type = typeMeeting; description = "Project continuation." },

            // Photography Club Events
            Event().apply { clubId = photoClub.id; name = "Campus Photo Walk"; date = LocalDate.of(2026, 4, 2); location = "Main Gate"; type = typeSocial; description = "A casual walk taking pictures of the campus." },
            Event().apply { clubId = photoClub.id; name = "Portrait Photography Tips"; date = LocalDate.of(2026, 4, 15); location = "Studio B"; type = typeWorkshop; description = "Learn lighting and posing." },
            Event().apply { clubId = photoClub.id; name = "End of Year Gallery"; date = LocalDate.of(2026, 6, 1); location = "Art Gallery"; type = typeOther; description = "Showcasing student work." },

            // Hiking Club Events
            Event().apply { clubId = hikingClub.id; name = "Sunrise Mountain Hike"; date = LocalDate.of(2026, 3, 25); location = "Base Camp"; type = typeSocial; description = "Early morning hike with breakfast." },
            Event().apply { clubId = hikingClub.id; name = "Survival Skills Workshop"; date = LocalDate.of(2026, 4, 18); location = "Forest Reserve"; type = typeWorkshop; description = "Learn to build a fire and navigate." },
            Event().apply { clubId = hikingClub.id; name = "Semester Planning Meeting"; date = LocalDate.of(2026, 3, 5); location = "Room C202"; type = typeMeeting; description = "Voting on next destinations." },

            // Film Society Events
            Event().apply { clubId = filmSociety.id; name = "Sci-Fi Movie Marathon"; date = LocalDate.of(2026, 5, 1); location = "Theater Room"; type = typeSocial; description = "Back-to-back alien movies." },
            Event().apply { clubId = filmSociety.id; name = "Director's Cut Discussion"; date = LocalDate.of(2026, 4, 10); location = "Room 105"; type = typeTalk; description = "Analyzing famous directing styles." },
            Event().apply { clubId = filmSociety.id; name = "Short Film Competition"; date = LocalDate.of(2026, 6, 20); location = "Auditorium"; type = typeCompetition; description = "Submit your 5-minute films." }
        )

        eventRepository.saveAll(initialEvents)

        println("Database seeded successfully with Types, Clubs, and Events!")
    }
}