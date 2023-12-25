package hua.dit.localDocWebApp.service;

import hua.dit.localDocWebApp.entity.Family;
import hua.dit.localDocWebApp.repository.FamilyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FamilyService {

    @Autowired
    private FamilyRepository familyRepository;


    @Transactional
    public void saveFamilyMember(Family family){
        familyRepository.save(family);
    }


    @Transactional
    public Iterable<Family> getFamilyMembers(Integer client_id){
        return familyRepository.findByClient_Id(client_id);
    }

    @Transactional
    public void deleteFamilyMember(Integer id){
        familyRepository.deleteById(id);
    }


    @Transactional
    public Family getFamilyMember(Integer id){
        return familyRepository.findById(id).get();
    }


}
