package hua.dit.localDocWebApp.controller;

import hua.dit.localDocWebApp.config.JwtUtils;
import hua.dit.localDocWebApp.entity.Role;
import hua.dit.localDocWebApp.entity.User;
import hua.dit.localDocWebApp.payload.request.LoginRequest;
import hua.dit.localDocWebApp.payload.request.SignupRequest;
import hua.dit.localDocWebApp.payload.response.JwtResponse;
import hua.dit.localDocWebApp.payload.response.MessageResponse;
import hua.dit.localDocWebApp.repository.RoleRepository;
import hua.dit.localDocWebApp.repository.UserRepository;
import hua.dit.localDocWebApp.service.UserDetailsImpl;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;



    @PostMapping("/signin") //sign in
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println("authentication");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())); //authenticates the user
        System.out.println("authentication: " + authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication); //sets the authentication
        System.out.println("post authentication");
        String jwt = jwtUtils.generateJwtToken(authentication);//generates the jwt token
        System.out.println("jw: " + jwt);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal(); //gets the user details
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList()); //gets the roles of the user

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup") //sign up
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) { //checks if the username already exists
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {//checks if the email already exists
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = new HashSet<>();

        if (signUpRequest.getUserRole() == null) { //if the user role is not defined, it is a client
            Role userRole = roleRepository.findByName("ROLE_CLIENT")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else { //if the user role is defined, it is either a doctor or an admin
                switch (signUpRequest.getUserRole()) {
                    case "ROLE_ADMIN":
                        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "ROLE_DOCTOR":
                        Role modRole = roleRepository.findByName("ROLE_DOCTOR")
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    case "ROLE_CLIENT":
                        Role userRole = roleRepository.findByName("ROLE_CLIENT")
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
        }

        user.setRoles(roles);
        userRepository.save(user); //saves the user

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
