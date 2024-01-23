package hua.dit.localDocWebApp.rest;

import hua.dit.localDocWebApp.entity.Client;
import hua.dit.localDocWebApp.entity.Doctor;
import hua.dit.localDocWebApp.entity.User;
import hua.dit.localDocWebApp.service.ClientService;
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
@RequestMapping("/api/client")
public class ClientRestController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    UserService userService;

    @GetMapping("/list")
    public ResponseEntity<List<Client>> showClientList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        User user = userService.getUserByEmail(email);
        List<Client> clients = clientService.getClientsByUser(user);
        return clients != null ? ResponseEntity.ok(clients) : ResponseEntity.notFound().build();
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<Client> getClient(@PathVariable Integer id) {
        Client client = clientService.getClient(id);
        return client != null ? ResponseEntity.ok(client) : ResponseEntity.notFound().build();
    }


    @PostMapping("/list/{client_id}/removeDoc/{doctor_id}")
    public Client removeClientDoctor(@PathVariable Integer client_id, @PathVariable Integer doctor_id){
        Client client = clientService.removeClientDoctor(client_id);
        doctorService.decreaseCurrentPatients(doctor_id);
        return client;
    }

    @GetMapping("/list/doc/{postalCode}/{client_id}")
    public ResponseEntity<Doctor> showDocList(@PathVariable String postalCode, @PathVariable Integer client_id){
        Iterable<Doctor> doctors = doctorService.getDoctorByPostalCode(postalCode);

        return ResponseEntity.ok((Doctor) doctors);

    }

    @GetMapping("/new")
    @ResponseBody
    public ResponseEntity<Client> addClient() {
        Client client = new Client();

        return ResponseEntity.ok(client);
    }

    @PostMapping("/new")
    public ResponseEntity<String> saveClient(@RequestBody Client client) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            User user = userService.getUserByEmail(email);
            client.setUser(user);
            clientService.saveClient(client);
            return ResponseEntity.ok("Client saved successfully");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }
    }

}
