package pt.unl.fct.iadi.novaevents.security

import jakarta.servlet.http.Cookie
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Allows @PreAuthorize annotations
class SecurityConfig(
    private val jwtFilter: JwtAuthenticationFilter,
    private val jwtService: JwtService
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        // 1. Stateless Session & CSRF via Cookie (needed for stateless apps)
        http.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        http.csrf { it.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) }

        // 2. URL Authorization Rules
        http.authorizeHttpRequests { auth ->
            // We include "delete" here so owners (EDITORs) can reach the @PreAuthorize method check
            auth.requestMatchers(
                "/clubs/*/events/new",
                "/clubs/*/events/*/edit",
                "/clubs/*/events/*/delete"
            ).hasAnyRole("EDITOR", "ADMIN")

            // 2. BROAD/PUBLIC RULES COME SECOND
            auth.requestMatchers(HttpMethod.GET, "/clubs/**", "/events/**").permitAll()
            auth.requestMatchers("/login", "/css/**", "/js/**").permitAll()

            // 3. CATCH-ALL
            auth.anyRequest().authenticated()
        }

        // 3. Login Configuration (Custom Success Handler for JWT)
        http.formLogin { form ->
            form.loginPage("/login")
            form.successHandler { _, response, authentication ->
                val token = jwtService.generateToken(authentication)
                val cookie = Cookie("jwt", token).apply {
                    isHttpOnly = true
                    path = "/"
                }
                response.addCookie(cookie)
                response.sendRedirect("/clubs")
            }
            form.permitAll()
        }

        // 4. Logout Configuration
        http.logout { logout ->
            logout.logoutUrl("/logout")
            logout.logoutSuccessHandler { _, response, _ ->
                val cookie = Cookie("jwt", "").apply {
                    isHttpOnly = true
                    path = "/"
                    maxAge = 0 // Deletes the cookie
                }
                response.addCookie(cookie)
                response.sendRedirect("/clubs")
            }
        }

        // 5. Add JWT Filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}