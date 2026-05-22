package kfu.itis.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (statusCode != null) {
            int status = Integer.parseInt(statusCode.toString());

            if (status == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("status", 404);
                model.addAttribute("message", "Страница не найдена");
                return "error/404";
            } else if (status == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("status", 403);
                model.addAttribute("message", "Доступ запрещен");
                return "error/403";
            } else if (status == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("status", 500);
                model.addAttribute("message", "Внутренняя ошибка сервера");
                return "error/500";
            }
        }

        model.addAttribute("status", 500);
        model.addAttribute("message", "Произошла непредвиденная ошибка");
        return "error/500";
    }
}