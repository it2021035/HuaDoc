package hua.dit.localDocWebApp.controller;

import hua.dit.localDocWebApp.entity.Client;
import hua.dit.localDocWebApp.service.ClientService;
import hua.dit.localDocWebApp.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private DoctorService doctorService;


    //shows the add a new client form
    @GetMapping("/new")
    public String addClient(Model model){
        Client client = new Client();
        model.addAttribute("client", client);
        return "add_client";
    }


    //saves the new client in db
    @PostMapping("/new")
    public String saveClient(Client client, Model model){
        clientService.saveClient(client);
        return "home";
    }




    //lists all the clients
    @GetMapping("/list")
    public String showClientList(Model model){
        model.addAttribute("clients", clientService.getClients());
        return "client_list";
    }
    //



    //lists all the doctors that have the same postal code as the client
    @GetMapping("/list/doc/{postalCode}/{client_id}")
    public String showDocList(Model model, @PathVariable String postalCode, @PathVariable Integer client_id){
        model.addAttribute("doctors", doctorService.getDoctorByPostalCode(postalCode));
        model.addAttribute("clients", clientService.getClient(client_id));
        return "doctor_list";
    }


    //not being used
    @GetMapping("/list/doc/{postalCode}/{client_id}/{doctor_id}")
    public String saveClientDoctor(@PathVariable String postalCode, @PathVariable Integer client_id, @PathVariable Integer doctor_id){
        doctorService.saveClientDoctor(client_id, doctor_id);
        return "home";
    }


}
