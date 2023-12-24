package hua.dit.localDocWebApp.service;

import hua.dit.localDocWebApp.entity.Client;
import hua.dit.localDocWebApp.entity.Doctor;
import hua.dit.localDocWebApp.entity.PendingAproval;
import hua.dit.localDocWebApp.repository.DoctorRepository;
import hua.dit.localDocWebApp.repository.PendingAprovalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PendingAprovalService {
    @Autowired
    private PendingAprovalRepository pendingAprovalRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private DoctorRepository doctorRepository;


    @Transactional
    public void deletePendingAproval(Integer id){
        pendingAprovalRepository.deleteById(id);
    }



    //get docker from id
    @Transactional
    public Doctor getDoctor(Integer id){
        return pendingAprovalRepository.findById(id).get().getDoctor();
    }


    //returns all the doctors that have pending aprovals
    @Transactional
    public List<PendingAproval> getPendingAprovals(){

        List<PendingAproval> temp = pendingAprovalRepository.findAll();
        for (int i = 0; i < temp.size(); i++) { //removes duplicates
            Integer currentElement = temp.get(i).getDoctor().getId();

            // Remove duplicates of the current element after it
            for (int j = i + 1; j < temp.size(); j++) {
                if (Objects.equals(currentElement, temp.get(j).getDoctor().getId())) {
                    temp.remove(j);
                    j--; // Adjust the index after removal
                }
            }
        }

        return temp;

    }

    //returns all the clients of a doctor
    @Transactional
    public List<PendingAproval> showClientsOfDoctor(Integer id){
        List<PendingAproval> temp = pendingAprovalRepository.findAll();
        //removes the duplicates of the same combination clien_id and doctor_id
        for (int i = 0; i < temp.size(); i++) {
            Integer currentElementClient = temp.get(i).getClient().getId();
            Integer currentElementDoctor = temp.get(i).getDoctor().getId();

            // Remove duplicates of the current element after it
            for (int j = i + 1; j < temp.size(); j++) {
                if (Objects.equals(currentElementClient, temp.get(j).getClient().getId()) && Objects.equals(currentElementDoctor, temp.get(j).getDoctor().getId())) {
                    temp.remove(j);
                    j--; // Adjust the index after removal
                }
            }
        }


        //makes a new list with only the clients of the doctor requested
        List<PendingAproval> temp2 = new ArrayList<>();
        for (int i = 0; i < temp.size(); i++) {
            System.out.println(temp.get(i).getClient().getId()+" "+temp.get(i).getDoctor().getId());
            if(temp.get(i).getDoctor().getId() == id){
                temp2.add(temp.get(i));
            }
        }


        return temp2;
    }





    @Transactional
    public void acceptClient(Integer doctorId, Integer clientId){
        //inserts the doctor id in the client doctord_id column
        List<PendingAproval> temp = pendingAprovalRepository.findAll();
        Client client = clientService.getClient(clientId);
        Doctor doctor = doctorRepository.findById(doctorId).get(); //FIX THIS LATER
        client.setDoctor(doctor);
        clientService.saveClient(client);
        //increases the current clients of the doctor
        doctor.setCurrentClients(doctor.getCurrentClients()+1);
        doctorRepository.save(doctor); //FIX THIS LATER

        //
        //removes the pending aproval for eveyone if that client id
        for (int i = 0; i < temp.size(); i++) {
            if(temp.get(i).getClient().getId() == clientId){
                pendingAprovalRepository.deleteById(temp.get(i).getId());
            }
        }

    }


    @Transactional
    public void deleteClient(Integer doctorId, Integer clientId){
        //removes the pending aproval
        List<PendingAproval> temp = pendingAprovalRepository.findAll();
        for (int i = 0; i < temp.size(); i++) {
            if(temp.get(i).getDoctor().getId() == doctorId && temp.get(i).getClient().getId() == clientId){
                pendingAprovalRepository.deleteById(temp.get(i).getId());
            }
        }
    }




    //function that saves the pending aproval with the client id and the doctor id
    @Transactional
    public void savePendingAproval(Client clientId, Doctor doctorId){
        PendingAproval pendingAproval = new PendingAproval();
        pendingAproval.setClient(clientId);
        pendingAproval.setDoctor(doctorId);
        pendingAprovalRepository.save(pendingAproval);

    }



}
