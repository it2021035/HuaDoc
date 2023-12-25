package hua.dit.localDocWebApp.controller;

import hua.dit.localDocWebApp.entity.Family;
import hua.dit.localDocWebApp.service.ClientService;
import hua.dit.localDocWebApp.service.FamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/family")
public class FamillyController {

    @Autowired
    private FamilyService familyService;

    @Autowired
    private ClientService clientService;


    @GetMapping("/list/{client_id}")
    public String listFamilyMembers(@PathVariable Integer client_id, Model model) {
        model.addAttribute("familys", familyService.getFamilyMembers(client_id));
        model.addAttribute("client_id", client_id);
        return "family_list";
    }


    //redirects you to a form for adding a new family member
    @GetMapping("/new/{client_id}")
    public String addFamilyMember(@PathVariable Integer client_id, Model model) {
        Family family = new Family();
        model.addAttribute("family", family);
        model.addAttribute("client_id", client_id);
        return "add_family";
    }


    //saves the new family member to the database
    @PostMapping("/new/{client_id}")
    public String saveFamilyMember(@PathVariable Integer client_id, Family family, Model model) {
        family.setClient(clientService.getClient(client_id));
        familyService.saveFamilyMember(family);
        return "redirect:/";
    }

    //deletes a family member from the database
    @PostMapping("/{id}/remove")
    public String deleteFamilyMember(@PathVariable Integer id, Model model) {
        familyService.deleteFamilyMember(id);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String editFamilyMember(@PathVariable Integer id, Model model) {
        model.addAttribute("family", familyService.getFamilyMember(id));
        model.addAttribute("client_id", familyService.getFamilyMember(id).getClient().getId());
        return "add_family";
    }

}
