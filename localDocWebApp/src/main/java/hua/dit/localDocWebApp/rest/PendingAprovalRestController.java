package hua.dit.localDocWebApp.rest;

import hua.dit.localDocWebApp.entity.Client;
import hua.dit.localDocWebApp.entity.Doctor;
import hua.dit.localDocWebApp.entity.PendingAproval;
import hua.dit.localDocWebApp.entity.User;
import hua.dit.localDocWebApp.repository.UserRepository;
import hua.dit.localDocWebApp.service.PendingAprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/pending")
public class PendingAprovalRestController {
    @Autowired
    private PendingAprovalService pendingAprovalService;


    @Autowired
    private UserRepository userRepository;


    //this is for the client to see
    @PostMapping("/insert/{clientId}/{doctorId}") //inserts a pending aproval
    public ResponseEntity<String> insertPendingAproval(@PathVariable Integer clientId, @PathVariable Integer doctorId){
        Client client = new Client();
        client.setId(clientId);
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        pendingAprovalService.savePendingAproval(client, doctor);
        return ResponseEntity.ok("Pending Aproval saved successfully");
    }

    //this is for the doctor to see
    @GetMapping("/show") //shows all the doctors with pending aprovals
    public ResponseEntity<List<Doctor>> showPendingAproval(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userName = userDetails.getUsername();
        User user = userRepository.findByUsername(userName);
        List<PendingAproval> pendingDocs =  pendingAprovalService.getPendingAprovals(user);
        List<Doctor> doctors = new ArrayList<>();
        for (PendingAproval pendingAproval : pendingDocs) {
           doctors.add(pendingAproval.getDoctor());
        }
        System.out.println(doctors);
        return doctors != null ? ResponseEntity.ok(doctors) : ResponseEntity.notFound().build();
    }

    //shows all the clients with pending aprovals for a specific doctor
    @GetMapping("/show/{doctorId}")
    public ResponseEntity<List<Client>> showPendingAprovalSpecific(@PathVariable Integer doctorId){
        List<PendingAproval> clientsPending =  pendingAprovalService.showClientsOfDoctor(doctorId);
        List<Client> clients = new ArrayList<>();
        for (PendingAproval pendingAproval : clientsPending) {
            clients.add(pendingAproval.getClient());
        }
        return clients != null ? ResponseEntity.ok(clients) : ResponseEntity.notFound().build();
    }

    @PostMapping("/show/{doctorId}/{clientId}") //accepts the client
    public ResponseEntity<String> acceptClient(@PathVariable Integer doctorId, @PathVariable Integer clientId){
        String result = pendingAprovalService.acceptClient(doctorId, clientId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/show/{doctorId}/{clientId}/decline") //declines the client
    public ResponseEntity<String> declineClient(@PathVariable Integer doctorId, @PathVariable Integer clientId){
        pendingAprovalService.deleteClient(doctorId, clientId);
        return ResponseEntity.ok("Client declined successfully");
    }


}
