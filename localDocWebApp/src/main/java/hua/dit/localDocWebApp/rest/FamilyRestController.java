package hua.dit.localDocWebApp.rest;

import hua.dit.localDocWebApp.entity.Client;
import hua.dit.localDocWebApp.entity.Family;
import hua.dit.localDocWebApp.service.ClientService;
import hua.dit.localDocWebApp.service.FamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/family")
public class FamilyRestController {
    @Autowired
    private FamilyService familyService;

    @Autowired
    private ClientService clientService;


    @GetMapping("/list/{client_id}")
    public ResponseEntity<List<Family>> listFamilyMembers(@PathVariable Integer client_id) {
        Iterable<Family> familyList = familyService.getFamilyMembers(client_id);
        return familyList != null ? ResponseEntity.ok((List<Family>) familyList) : ResponseEntity.notFound().build();
    }
    //saves the new family member to the database
    @PostMapping("/new/{client_id}")
    public ResponseEntity<String> saveFamilyMember(@PathVariable Integer client_id,@RequestBody Family family) {
        family.setClient(clientService.getClient(client_id));
        familyService.saveFamilyMember(family);
        return ResponseEntity.ok("Family member saved successfully");
    }

    //deletes a family member from the database
    @PostMapping("/{familyId}/remove")
    public ResponseEntity<String> deleteFamilyMember(@PathVariable Integer familyId) {
        familyService.deleteFamilyMember(familyId);
        return ResponseEntity.ok("Family member deleted successfully");
    }


    @GetMapping("/{familyId}")
    public ResponseEntity<Family> editFamilyMember(@PathVariable Integer familyId) {
        Family family =  familyService.getFamilyMember(familyId);
        return family != null ? ResponseEntity.ok(family) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{familyId}/{clientId}/edit")
    public ResponseEntity<String> removeAndReApplyFamilyMember(@PathVariable Integer client_id,@RequestBody Family family) {
        Integer familyId = family.getId();
        familyService.deleteFamilyMember(familyId);
        family.setClient(clientService.getClient(client_id));
        familyService.saveFamilyMember(family);
        return ResponseEntity.ok("Family member Edited successfully");
    }


}
