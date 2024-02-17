package hua.dit.localDocWebApp.repository;

import hua.dit.localDocWebApp.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    Iterable<Doctor> findByPostalCode(String postalCode);

    List<Doctor> findByEmail(String email);
}
