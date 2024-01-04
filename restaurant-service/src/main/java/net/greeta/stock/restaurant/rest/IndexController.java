package net.greeta.stock.restaurant.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @Value("${spring.keycloak.server-external-url}")
    private String keycloakServerExternalUrl;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("keycloakServerUrl", keycloakServerExternalUrl);
        return "index";
    }
}
