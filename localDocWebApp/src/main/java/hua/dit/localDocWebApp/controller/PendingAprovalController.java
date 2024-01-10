package hua.dit.localDocWebApp.controller;

import hua.dit.localDocWebApp.entity.Client;
import hua.dit.localDocWebApp.entity.Doctor;
import hua.dit.localDocWebApp.entity.PendingAproval;
import hua.dit.localDocWebApp.entity.User;
import hua.dit.localDocWebApp.service.PendingAprovalService;
import hua.dit.localDocWebApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/pending")
public class PendingAprovalController {

    @Autowired
    private PendingAprovalService pendingAprovalService;

    @Autowired
    private UserService userService;

    //this puts the client in the pending list for the chosen doctor
    //happened when the client clicks the button "request aproval" in the doctor list
    @PostMapping("/insert/{clientId}/{doctorId}")
    public String insertPendingAproval(@PathVariable Integer clientId, @PathVariable Integer doctorId){
        Client client = new Client();
        client.setId(clientId);
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        pendingAprovalService.savePendingAproval(client, doctor);
        return "redirect:/";
    }


    @PostMapping("/show/{doctorId}/{clientId}")
    public String acceptClient(@PathVariable Integer doctorId, @PathVariable Integer clientId){
        pendingAprovalService.acceptClient(doctorId, clientId);
        return "redirect:/";
    }

    @PostMapping("/show/{doctorId}/{clientId}/decline")
    public String declineClient(@PathVariable Integer doctorId, @PathVariable Integer clientId){
        pendingAprovalService.deleteClient(doctorId, clientId);
        return "redirect:/";
    }


    //shows all the doctors with pending aprovals
    @GetMapping("/show")
    public String showPendingAproval(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        User user = userService.getUserByEmail(email);

        model.addAttribute("pendingAprovals", pendingAprovalService.getPendingAprovals(user));
        return "pending_doctor_list";
    }
    //shows all the clients with pending aprovals for a specific doctor
    @GetMapping("/show/{doctorId}")
    public String showPendingAprovalSpecific(Model model, @PathVariable Integer doctorId){
        model.addAttribute("pendingAprovalsClients", pendingAprovalService.showClientsOfDoctor(doctorId));
        return "pending_doctor_list_client";
    }





}
