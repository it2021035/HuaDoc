package hua.dit.localDocWebApp.rest;


import hua.dit.localDocWebApp.entity.Client;
import hua.dit.localDocWebApp.entity.User;
import hua.dit.localDocWebApp.repository.RoleRepository;
import hua.dit.localDocWebApp.repository.UserRepository;
import hua.dit.localDocWebApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserRestController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/register")
    @ResponseBody
    public ResponseEntity<User> register(Model model) {
        User user = new User();

        return ResponseEntity.ok(user);
    }

    @PostMapping("/saveUser")
    public ResponseEntity<String> saveUser(@RequestBody User user){
        Integer id = userService.saveUser(user);
        return ResponseEntity.ok("User '"+id+"' saved successfully !");
    }

}
