package org.eida.SMART.c;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.eida.SMART.m.User;
import org.eida.SMART.m.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MyErrorController implements ErrorController {

    @Autowired
    private UserDAO userDAO;

    /**
     * Handle HTTP 500 Internal Server error
     * @return view
     */
    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Principal principal) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                User user = userDAO.findByEmail(MainController.currentUserName(principal));
                if (status != null) {

                    if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                        if (user.getAccessLevel() == 0) {
                            return "redirect:/sadmin";
                        } else if (user.getAccessLevel() == 1) {
                            return "redirect:/admin";
                        } else if (user.getAccessLevel() == 2) {
                            return "redirect:/client";
                        }
                        return "redirect:/login";
                    }
                }
            }
        }
        return "redirect:/login";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}