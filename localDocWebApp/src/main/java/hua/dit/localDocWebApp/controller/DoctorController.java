package hua.dit.localDocWebApp.controller;

import hua.dit.localDocWebApp.entity.Doctor;
import hua.dit.localDocWebApp.entity.User;
import hua.dit.localDocWebApp.service.DoctorService;
import hua.dit.localDocWebApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserService userService;


    //shows the add a new doctor form
    @GetMapping("/new")
    public String addDoctor(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        User user = userService.getUserByEmail(email);
        model.addAttribute("User", user);

        Doctor doctor = new Doctor();
        model.addAttribute("doctor", doctor);
        return "add_doctor";
    }
    //saves the new doctor in db
    @PostMapping("/new/{userId}")
    public String saveDoctor(@PathVariable Long userId, Doctor doctor, Model model){
        try {
            User user = (User) userService.getUser(userId);
            doctor.setUser(user);
            doctorService.saveDoctor(doctor);
            return "redirect:/";
        }catch (DataIntegrityViolationException e){
            model.addAttribute("error", "Email already exists");
            return "add_doctor";
        }
    }

    //lists all the doctors
    @GetMapping("/list")
    public String showDocList(Model model){
        model.addAttribute("doctors", doctorService.getDoctors());
        return "doctor_list";
    }

}
