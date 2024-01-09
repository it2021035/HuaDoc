package hua.dit.localDocWebApp.rest;

import hua.dit.localDocWebApp.entity.Client;
import hua.dit.localDocWebApp.service.ClientService;
import hua.dit.localDocWebApp.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
public class ClientRestController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private DoctorService doctorService;

    @GetMapping("")
    public Iterable<Client> getClients() {
        return clientService.getClients();
    }

    @PostMapping("")
    public Client saveClient(@RequestBody Client client) {
        return clientService.saveClient(client);
    }

    @PostMapping("/list/{client_id}/removeDoc/{doctor_id}")
    public Client removeClientDoctor(@PathVariable Integer client_id, @PathVariable Integer doctor_id){
        Client client = clientService.removeClientDoctor(client_id);
        doctorService.decreaseCurrentPatients(doctor_id);
        return client;
    }

}
