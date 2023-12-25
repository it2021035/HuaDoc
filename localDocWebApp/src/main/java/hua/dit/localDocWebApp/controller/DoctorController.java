package hua.dit.localDocWebApp.controller;

import hua.dit.localDocWebApp.entity.Doctor;
import hua.dit.localDocWebApp.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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


    //shows the add a new doctor form
    @GetMapping("/new")
    public String addDoctor(Model model){
        Doctor doctor = new Doctor();
        model.addAttribute("doctor", doctor);
        return "add_doctor";
    }
    //saves the new doctor in db
    @PostMapping("/new")
    public String saveDoctor(Doctor doctor, Model model){
        try {
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
