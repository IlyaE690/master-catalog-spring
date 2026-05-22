package kfu.itis.config;

import kfu.itis.model.entity.User;
import kfu.itis.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class GlobalModelAttributes {

    private final UserService userService;

    public GlobalModelAttributes(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("currentUser")
    public User getCurrentUser(Principal principal) {
        if (principal == null) {
            return null;
        }
        return userService.findByUsername(principal.getName()).orElse(null);
    }

    @ModelAttribute("isCustomer")
    public boolean isCustomer() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return false;
        }
        return auth.getAuthorities().stream()
                .anyMatch(granted -> granted.getAuthority().equals("ROLE_CUSTOMER"));
    }

    @ModelAttribute("isMaster")
    public boolean isMaster() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return false;
        }
        return auth.getAuthorities().stream()
                .anyMatch(granted -> granted.getAuthority().equals("ROLE_MASTER"));
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return false;
        }
        return auth.getAuthorities().stream()
                .anyMatch(granted -> granted.getAuthority().equals("ROLE_ADMIN"));
    }

    @ModelAttribute("isAuthenticated")
    public boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser");
    }
}