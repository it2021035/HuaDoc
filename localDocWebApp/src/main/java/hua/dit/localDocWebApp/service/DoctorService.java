package hua.dit.localDocWebApp.service;

import hua.dit.localDocWebApp.entity.Doctor;
import hua.dit.localDocWebApp.repository.DoctorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
