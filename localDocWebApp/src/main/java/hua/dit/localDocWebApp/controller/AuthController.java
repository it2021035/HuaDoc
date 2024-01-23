package hua.dit.localDocWebApp.controller;

import hua.dit.localDocWebApp.entity.Role;
import hua.dit.localDocWebApp.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void setup(){
        roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
            roleRepository.save(new Role("ROLE_ADMIN"));
            return null;
        });
        roleRepository.findByName("ROLE_CLIENT").orElseGet(() -> {
            roleRepository.save(new Role("ROLE_CLIENT"));
            return null;
        });
        roleRepository.findByName("ROLE_DOCTOR").orElseGet(() -> {
            roleRepository.save(new Role("ROLE_DOCTOR"));
            return null;
        });

    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}