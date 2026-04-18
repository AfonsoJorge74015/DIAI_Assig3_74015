package pt.unl.fct.iadi.novaevents.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class LoggingInterceptor : HandlerInterceptor {
    private val logger = LoggerFactory.getLogger(LoggingInterceptor::class.java)

    override fun afterCompletion(
        request: HttpServletRequest, response: HttpServletResponse,
        handler: Any, ex: Exception?
    ) {
        val auth = SecurityContextHolder.getContext().authentication
        val principal = if (auth != null && auth.isAuthenticated && auth.principal != "anonymousUser") {
            auth.name
        } else {
            "anonymous"
        }

        // Format: [<principal>] METHOD URI [status]
        logger.info("[$principal] ${request.method} ${request.requestURI} [${response.status}]")
    }
}