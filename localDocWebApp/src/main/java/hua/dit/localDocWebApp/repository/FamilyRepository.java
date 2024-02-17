package hua.dit.localDocWebApp.repository;

import hua.dit.localDocWebApp.entity.Client;
import hua.dit.localDocWebApp.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyRepository extends JpaRepository<Family, Integer> {

        Iterable<Family> findByClient_Id(Integer client_id);

        List<Family> findByClient(Client client);
}
