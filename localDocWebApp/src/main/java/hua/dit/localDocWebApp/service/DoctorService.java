package hua.dit.localDocWebApp.service;

import hua.dit.localDocWebApp.entity.Client;
import hua.dit.localDocWebApp.entity.Doctor;
import hua.dit.localDocWebApp.repository.ClientRepository;
import hua.dit.localDocWebApp.repository.DoctorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ClientService clientService;




    @Transactional
    public void deleteDoctor(Integer id){
        doctorRepository.deleteById(id);
    } //detetes doctor

    @Transactional
    public void saveDoctor(Doctor doctor){
        doctorRepository.save(doctor);
    } //saves doctor

    @Transactional
    public Doctor getDoctor(Integer id){
        return doctorRepository.findById(id).get();
    } //get doctor from id

    @Transactional
    public Iterable<Doctor> getDoctors(){
        return doctorRepository.findAll();
    } //get all doctors

    //get all doctors that have the same postal code as the client
    @Transactional
    public Iterable<Doctor> getDoctorByPostalCode(String postalCode){
        return doctorRepository.findByPostalCode(postalCode);
    }

    //in the client insert the doctors id
    @Transactional
    public void saveClientDoctor(Integer clientId, Integer doctorId){
        Client client = clientService.getClient(clientId);
        Doctor doctor = doctorRepository.findById(doctorId).get();
        client.setDoctor(doctor);
        clientService.saveClient(client);
    }


    //decreases the current number of patients of the doctor by 1
    @Transactional
    public void decreaseCurrentPatients(Integer doctorId){
        Doctor doctor = doctorRepository.findById(doctorId).get();
       doctor.setCurrentClients(doctor.getCurrentClients()-1);
        doctorRepository.save(doctor);
    }



}
