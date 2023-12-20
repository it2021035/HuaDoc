package hua.dit.localDocWebApp.controller;

import hua.dit.localDocWebApp.entity.Doctor;
import hua.dit.localDocWebApp.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/new")
    public String addDoctor(Model model){
        Doctor doctor = new Doctor();
        model.addAttribute("doctor", doctor);
        return "add_doctor";
    }

    @PostMapping("/new")
    public String saveDoctor(Doctor doctor, Model model){
        doctorService.saveDoctor(doctor);
        return "home";
    }

}
