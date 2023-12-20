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


    @Transactional
    public void deleteDoctor(Integer id){
        doctorRepository.deleteById(id);
    }

    @Transactional
    public void saveDoctor(Doctor doctor){
        doctorRepository.save(doctor);
    }

    @Transactional
    public Doctor getDoctor(Integer id){
        return doctorRepository.findById(id).get();
    }

    @Transactional
    public Iterable<Doctor> getDoctors(){
        return doctorRepository.findAll();
    }

    //get all doctors that have the same postal code as the client
    @Transactional
    public Iterable<Doctor> getDoctorByPostalCode(String postalCode){
        return doctorRepository.findByPostalCode(postalCode);
    }
}
