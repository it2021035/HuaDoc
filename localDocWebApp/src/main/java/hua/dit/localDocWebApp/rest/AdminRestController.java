package hua.dit.localDocWebApp.rest;


import hua.dit.localDocWebApp.entity.*;
import hua.dit.localDocWebApp.repository.ClientRepository;
import hua.dit.localDocWebApp.repository.UserRepository;
import hua.dit.localDocWebApp.service.ClientService;
import hua.dit.localDocWebApp.service.DoctorService;
import hua.dit.localDocWebApp.service.FamilyService;
import hua.dit.localDocWebApp.service.PendingAprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private FamilyService familyService;

    @Autowired
    private PendingAprovalService pendingAprovalService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> showUsers(){
        List<User> users = userRepository.findAll();
        return users != null ? ResponseEntity.ok((users)) : ResponseEntity.notFound().build();
    }


    @PostMapping("/users/{userId}/remove")
    public ResponseEntity<String> removeUser(@PathVariable Long userId){
        Set<Role> roles = userRepository.findById(userId).get().getRoles();
        Role role = roles.iterator().next();

        if(role.getName().equals("ROLE_CLIENT")){
            Iterable<Client> clients = clientService.getClients();
            for(Client client : clients){
                if(Objects.equals(client.getUser().getId(), userId)){
                    Iterable<Family> families = familyService.getFamilyMembers(client.getId());
                    if(families != null){
                        for(Family family : families){
                        familyService.deleteFamilyMember(family.getId());
                        }
                    }
                    Iterable<PendingAproval> pendingAprovals = pendingAprovalService.getAllPendingAprovals();
                    if(pendingAprovals != null){
                        for(PendingAproval pendingAproval : pendingAprovals){
                            if(Objects.equals(pendingAproval.getClient().getId(), client.getId())){
                                pendingAprovalService.deletePendingAproval(pendingAproval.getId());
                            }
                        }
                    }
                    clientService.deleteClient(client.getId());
                }
            }
        }
        else if(role.getName().equals("ROLE_DOCTOR")){
            Iterable<Doctor> doctors = doctorService.getDoctors();
            for(Doctor doctor : doctors){
                if(Objects.equals(doctor.getUser().getId(), userId)){
                    Iterable<PendingAproval> pendingAprovals = pendingAprovalService.getAllPendingAprovals();
                    if(pendingAprovals != null){
                        for(PendingAproval pendingAproval : pendingAprovals){
                            if(Objects.equals(pendingAproval.getDoctor().getId(), doctor.getId())){
                                pendingAprovalService.deletePendingAproval(pendingAproval.getId());
                            }
                        }
                    }
                    doctorService.deleteDoctor(doctor.getId());
                }
            }
        }

        userRepository.deleteById(userId);
        return ResponseEntity.ok("User removed successfully");
    }


}
