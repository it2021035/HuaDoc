package hua.dit.localDocWebApp.rest;


import hua.dit.localDocWebApp.entity.Client;
import hua.dit.localDocWebApp.entity.Role;
import hua.dit.localDocWebApp.entity.User;
import hua.dit.localDocWebApp.payload.MessageResponse;
import hua.dit.localDocWebApp.payload.SignupRequest;
import hua.dit.localDocWebApp.repository.RoleRepository;
import hua.dit.localDocWebApp.repository.UserRepository;
import hua.dit.localDocWebApp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class UserRestController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @GetMapping("/register")
    @ResponseBody
    public ResponseEntity<User> register(Model model) {
        User user = new User();

        return ResponseEntity.ok(user);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }


        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getUserRole());

        Set<Role> roles = new HashSet<>();

        if (signUpRequest.getUserRole() == null) {
            Role userRole = roleRepository.findByName("ROLE_CLIENT")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            switch (signUpRequest.getUserRole()) {
                case "ROLE_ADMIN":
                    Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);

                    break;
                case "ROLE_CLIENT":
                    Role modRole = roleRepository.findByName("ROLE_CLIENT")
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(modRole);

                    break;
                case "ROLE_DOCTOR":
                    Role userRole = roleRepository.findByName("ROLE_DOCTOR")
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);

                    break;
            }
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}
