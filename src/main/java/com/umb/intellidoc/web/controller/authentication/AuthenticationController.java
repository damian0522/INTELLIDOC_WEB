package com.umb.intellidoc.web.controller.authentication;

import com.umb.intellidoc.web.dto.authentication.LoginRequest;
import com.umb.intellidoc.web.dto.authentication.LoginResponse;
import com.umb.intellidoc.web.service.authentication.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(
            AuthenticationService authenticationService
    ) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "authentication/login";
    }

    @PostMapping("/login")
    public String login(
            LoginRequest request,
            Model model
    ) {

        try {

            LoginResponse response =
                    authenticationService.login(request);

            model.addAttribute("user", response);

            return "redirect:/";

        } catch (Exception exception) {

            model.addAttribute(
                    "error",
                    "Credenciales inválidas"
            );

            return "authentication/login";
        }
    }
}