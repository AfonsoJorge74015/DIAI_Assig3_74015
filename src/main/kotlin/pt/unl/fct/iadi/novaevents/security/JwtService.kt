package pt.unl.fct.iadi.novaevents.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService {
    // should be in application.properties
    private val key: SecretKey = Keys.hmacShaKeyFor("NovaEventsSuperSecretKeyWhichMustBeVeryLong32Bytes!".toByteArray())

    fun generateToken(authentication: Authentication): String {
        val roles = authentication.authorities.map { it.authority }
        return Jwts.builder()
            .subject(authentication.name)
            .claim("roles", roles)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + 86400000)) // 1 day
            .signWith(key)
            .compact()
    }

    fun extractUsername(token: String): String {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload.subject
    }

    fun extractRoles(token: String): List<String> {
        val claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload
        return claims.get("roles", List::class.java) as List<String>
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }
}