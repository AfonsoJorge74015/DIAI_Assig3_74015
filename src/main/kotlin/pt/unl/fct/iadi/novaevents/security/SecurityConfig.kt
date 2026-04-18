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
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val jwtFilter: JwtAuthenticationFilter,
    private val jwtService: JwtService
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        http.csrf { it.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) }

        http.authorizeHttpRequests { auth ->
            auth.requestMatchers(
                "/clubs/*/events/new",
                "/clubs/*/events/*/edit",
                "/clubs/*/events/*/delete"
            ).hasAnyRole("EDITOR", "ADMIN")

            auth.requestMatchers(HttpMethod.GET, "/clubs/**", "/events/**").permitAll()
            auth.requestMatchers("/login", "/css/**", "/js/**").permitAll()

            auth.anyRequest().authenticated()
        }

        http.formLogin { form ->
            form.loginPage("/login")
            form.successHandler { request, response, authentication ->
                val token = jwtService.generateToken(authentication)
                val jwtCookie = Cookie("jwt", token).apply {
                    isHttpOnly = true
                    path = "/"
                }
                response.addCookie(jwtCookie)

                val redirectCookie = request.cookies?.firstOrNull { it.name == "REDIRECT_URI" }
                val targetUrl = redirectCookie?.value ?: "/clubs"

                val clearCookie = Cookie("REDIRECT_URI", "").apply {
                    path = "/"
                    maxAge = 0
                    isHttpOnly = true
                }
                response.addCookie(clearCookie)

                response.sendRedirect(targetUrl)
            }
            form.permitAll()
        }

        http.logout { logout ->
            logout.logoutUrl("/logout")
            logout.logoutSuccessHandler { _, response, _ ->
                val cookie = Cookie("jwt", "").apply {
                    isHttpOnly = true
                    path = "/"
                    maxAge = 0
                }
                response.addCookie(cookie)
                response.sendRedirect("/clubs")
            }
        }

        http.exceptionHandling { exceptions ->
            val loginEntryPoint = LoginUrlAuthenticationEntryPoint("/login")

            exceptions.authenticationEntryPoint { request, response, authException ->
                val requestUri = request.requestURI
                val queryString = request.queryString
                val targetUrl = if (queryString != null) "$requestUri?$queryString" else requestUri

                val redirectCookie = Cookie("REDIRECT_URI", targetUrl).apply {
                    path = "/"
                    maxAge = 300
                    isHttpOnly = true
                }
                response.addCookie(redirectCookie)

                loginEntryPoint.commence(request, response, authException)
            }
        }

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}