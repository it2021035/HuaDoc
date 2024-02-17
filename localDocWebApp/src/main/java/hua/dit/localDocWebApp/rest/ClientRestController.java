package hua.dit.localDocWebApp.rest;

import hua.dit.localDocWebApp.entity.Client;
import hua.dit.localDocWebApp.entity.Doctor;
import hua.dit.localDocWebApp.entity.User;
import hua.dit.localDocWebApp.repository.UserRepository;
import hua.dit.localDocWebApp.service.ClientService;
import hua.dit.localDocWebApp.service.DoctorService;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/client")
public class ClientRestController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/list")
    public ResponseEntity<List<Client>> showClientList() { //shows all the clients of the user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userName = userDetails.getUsername();
        User user = userRepository.findByUsername(userName);
        List<Client> clients = clientService.getClientsByUser(user);
        return clients != null ? ResponseEntity.ok(clients) : ResponseEntity.notFound().build();
    }

    @GetMapping("/list/{id}") //shows the client with the specific id
    public ResponseEntity<Client> getClient(@PathVariable Integer id) {
        Client client = clientService.getClient(id);
        return client != null ? ResponseEntity.ok(client) : ResponseEntity.notFound().build();
    }


    @PostMapping("/list/{client_id}/removeDoc/{doctor_id}") //removes the doctor from the client
    public ResponseEntity<String> removeClientDoctor(@PathVariable Integer client_id, @PathVariable Integer doctor_id){
        Client client = clientService.removeClientDoctor(client_id);
        doctorService.decreaseCurrentPatients(doctor_id);
        return ResponseEntity.ok("Doctor removed successfully");
    }

    @GetMapping("/list/doc/{postalCode}/{client_id}") //shows the doctors of the specific postal code
    public ResponseEntity<List<Doctor>> showDocList(@PathVariable String postalCode, @PathVariable Integer client_id){
        Iterable<Doctor> doctors = doctorService.getDoctorByPostalCode(postalCode);
        return doctors != null ? ResponseEntity.ok((List<Doctor>) doctors) : ResponseEntity.notFound().build();

    }


    @PostMapping("/saveClient")
    public ResponseEntity<String> saveClient(@RequestBody Client client) { //saves the client
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String userName = userDetails.getUsername();
            User user = userRepository.findByUsername(userName);
            client.setUser(user);
            clientService.saveClient(client);
            return ResponseEntity.ok("Client saved successfully");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }
    }

}
