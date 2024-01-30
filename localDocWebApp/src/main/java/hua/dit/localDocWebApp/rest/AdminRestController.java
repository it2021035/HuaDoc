package hua.dit.localDocWebApp.rest;


import hua.dit.localDocWebApp.entity.Doctor;
import hua.dit.localDocWebApp.entity.User;
import hua.dit.localDocWebApp.repository.ClientRepository;
import hua.dit.localDocWebApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>> showUsers(){
        List<User> users = userRepository.findAll();
        return users != null ? ResponseEntity.ok((users)) : ResponseEntity.notFound().build();
    }

    //does not work yet because foreign key exception
    @PostMapping("/users/{userId}/remove")
    public ResponseEntity<String> removeUser(@PathVariable Long userId){
        userRepository.deleteById(userId);
        return ResponseEntity.ok("User removed successfully");
    }


}
