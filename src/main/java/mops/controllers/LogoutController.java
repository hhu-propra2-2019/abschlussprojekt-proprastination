package mops.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.annotation.SessionScope;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
@SessionScope
public class LogoutController {

    /**
     * The GetMapping for logging out
     *
     * @param request The HttpServletRequest
     * @return a redirect to /bewerbung2/
     * @throws ServletException If the logout fails
     */
    @GetMapping("/logout")
    public String logout(final HttpServletRequest request) throws ServletException {
        request.logout();
        return "redirect:/bewerbung2/";
    }
}
