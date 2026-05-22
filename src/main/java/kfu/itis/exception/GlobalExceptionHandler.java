package kfu.itis.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private boolean isAjax(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        String accept = request.getHeader("Accept");
        return "XMLHttpRequest".equals(requestedWith) ||
                (accept != null && accept.contains("application/json"));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Object handleNotFound(NoHandlerFoundException ex, HttpServletRequest request) {
        log.warn("Страница не найдена: {}", ex.getRequestURL());

        if (isAjax(request)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Страница не найдена"));
        }

        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("status", 404);
        mav.addObject("message", "Страница не найдена");
        return mav;
    }

    @ExceptionHandler(RuntimeException.class)
    public Object handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        log.error("Ошибка: {}", ex.getMessage(), ex);

        if (isAjax(request)) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ex.getMessage()));
        }

        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("message", "Внутренняя ошибка сервера: " + ex.getMessage());
        mav.addObject("status", 500);
        return mav;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("Некорректный запрос: {}", ex.getMessage());

        if (isAjax(request)) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }

        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("message", ex.getMessage());
        mav.addObject("status", 400);
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public Object handleAll(Exception ex, HttpServletRequest request) {
        log.error("Неизвестная ошибка: {}", ex.getMessage(), ex);

        if (isAjax(request)) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Внутренняя ошибка сервера"));
        }

        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("message", "Внутренняя ошибка сервера");
        mav.addObject("status", 500);
        return mav;
    }
}