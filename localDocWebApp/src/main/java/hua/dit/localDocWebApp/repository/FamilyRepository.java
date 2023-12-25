package hua.dit.localDocWebApp.repository;

import hua.dit.localDocWebApp.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyRepository extends JpaRepository<Family, Integer> {

        Iterable<Family> findByClient_Id(Integer client_id);
}
