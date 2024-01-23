package hua.dit.localDocWebApp.rest;

import hua.dit.localDocWebApp.entity.Client;
import hua.dit.localDocWebApp.entity.Doctor;
import hua.dit.localDocWebApp.entity.User;
import hua.dit.localDocWebApp.service.DoctorService;
import hua.dit.localDocWebApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorRestController {


    @Autowired
    private DoctorService doctorService;

    @Autowired
    UserService userService;

    @GetMapping("/new")
    @ResponseBody
    public ResponseEntity<Doctor> addDoctor() {
        Doctor doctor = new Doctor();

        return ResponseEntity.ok(doctor);
    }


    @PostMapping("/new")
    public ResponseEntity<String> saveDoctor(@RequestBody Doctor doctor) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            User user = userService.getUserByEmail(email);
            doctor.setUser(user);
            doctorService.saveDoctor(doctor);
            return ResponseEntity.ok("Doctor saved successfully");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Doctor> showDocList(Model model){
        Iterable<Doctor> doctors = doctorService.getDoctors();
        return ResponseEntity.ok((Doctor) doctors);
    }
}
