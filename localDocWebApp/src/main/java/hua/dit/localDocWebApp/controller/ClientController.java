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

    @GetMapping("/new")
    public String addClient(Model model){
        Client client = new Client();
        model.addAttribute("client", client);
        return "add_client";
    }

    @PostMapping("/new")
    public String saveClient(Client client, Model model){
        clientService.saveClient(client);
        return "home";
    }

    @GetMapping("/list/doc")
    public String showDocList(Model model){
        model.addAttribute("doctors", doctorService.getDoctors());
        return "doctor_list";
    }


    @GetMapping("/list")
    public String showClientList(Model model){
        model.addAttribute("clients", clientService.getClients());
        return "client_list";
    }

    @GetMapping("/list/doc/{postalCode}")
    public String showDocList(Model model, @PathVariable String postalCode){
        model.addAttribute("doctors", doctorService.getDoctorByPostalCode(postalCode));
        return "doctor_list";
    }


}
