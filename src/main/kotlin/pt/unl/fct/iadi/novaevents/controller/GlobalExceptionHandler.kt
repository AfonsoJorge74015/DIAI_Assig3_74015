package pt.unl.fct.iadi.novaevents.controller

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.ModelAndView
import java.util.NoSuchElementException

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(e: NoSuchElementException): ModelAndView {
        val mav = ModelAndView("error/404")
        mav.addObject("errorMessage", e.message)
        return mav
    }
}